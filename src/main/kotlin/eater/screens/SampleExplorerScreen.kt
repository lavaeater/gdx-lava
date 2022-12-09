package eater.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.math.MathUtils.floor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import de.pottgames.tuningfork.Audio
import de.pottgames.tuningfork.SoundBuffer
import de.pottgames.tuningfork.WaveLoader
import eater.core.MainGame
import eater.core.SelectedItemList
import eater.core.selectedItemListOf
import eater.extensions.boundLabel
import eater.injection.InjectionContext.Companion.inject
import eater.input.CommandMap
import eater.music.ListItem
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ktx.actors.stage
import ktx.collections.lastIndex
import ktx.collections.toGdxArray
import ktx.scene2d.*
import space.earlygrey.shapedrawer.ShapeDrawer

class SampleExplorerScreen(game: MainGame) : BasicScreen(game, CommandMap("MyCommands")) {

    private val sampleBaseDir = "projects/games/music-samples-explorer"
    private fun getSamples(): ListItem.Directory {
        val root = ListItem.Directory(
            "music-samples",
            sampleBaseDir, null)
        getDirInfo(root)
        return root
    }

    val noteMin = 60 //one octave lower
    val noteMax = 84 //one octave higher
    var currentNote = 0 //72 should equal a pitch of around 1f, but I have no idea

    val pitchSpan = (60 - 72)..(84 - 72)

    /*
    I have an idea.

    The min pitch is 0.5, the max 2.0. This equals... it's so low, like the root from 2
     */

    override val viewport: Viewport = ExtendViewport(400f, 600f)

    private val sampleStore by lazy { getSamples() }
    private var currentDirectory = sampleStore

    private fun getDirInfo(directory: ListItem.Directory) {
        val dirContents = Gdx.files.external(directory.path).list()
        val dirs = dirContents.filter { it.isDirectory }
        for (dir in dirs) {
            val d = ListItem.Directory(dir.name(), dir.path(), directory)
            directory.childDirs.add(d)
            getDirInfo(d)
        }
        val files = dirContents.filter { !it.isDirectory && it.extension() == "wav" }
        for (file in files) {
            directory.files.add(ListItem.SoundFile(file.nameWithoutExtension(), file.path()))
        }
    }


    private var currentListIsDirList = true
    private val currentListIsFileList get() = !currentListIsDirList

    private fun switchList() {
        currentList = if (currentListIsDirList) fileList else dirList
        currentListIsDirList = !currentListIsDirList
    }

    private fun goUpInCurrentList() {
        if (currentList.selectedIndex == 0)
            currentList.selectedIndex = 0
        else if (currentList.selectedIndex >= 1)
            currentList.selectedIndex--
    }

    private fun goDownInCurrentList() {
        if (currentList.selectedIndex == currentList.items.lastIndex)
            currentList.selectedIndex = currentList.items.lastIndex
        else if (currentList.selectedIndex < currentList.items.lastIndex)
            currentList.selectedIndex++
    }

    private val sounds = mutableMapOf<ListItem.SoundFile, SoundBuffer>()

    private val audio by lazy { inject<Audio>() }

    private fun selectCurrentItemInList() {
        when (currentList.selected) {
            is ListItem.Directory -> {
                currentDirectory = currentList.selected as ListItem.Directory
                updateFilesAndFoldersLists()
            }

            is ListItem.SoundFile -> {
                /*
                Play this fudging sound, mate!
                 */
                tryToPlay(currentList.selected as ListItem.SoundFile)

            }
        }
    }

    fun getFiles(output: MutableList<ListItem.SoundFile>, input: ListItem.Directory) {
        for(dir in input.childDirs)
            getFiles(output, dir)
        output.addAll(input.files)
    }

    private fun flattenAllSamples(): SelectedItemList<ListItem.SoundFile> {
        val list = mutableListOf<ListItem.SoundFile>()
        getFiles(list, sampleStore)
        return selectedItemListOf(*list.toTypedArray())
    }

    private val allSamples by lazy { flattenAllSamples() }

    private fun tryToPlay(soundFile: ListItem.SoundFile) {
        var soundWorks = true

        if (!sounds.containsKey(soundFile)) {
            try {
                sounds[soundFile] = WaveLoader.load(Gdx.files.external(soundFile.path))
            } catch (e: Exception) {
                soundWorks = false
            }
        }

        if (soundWorks) {
            val sound = sounds[soundFile]!!
            audio.play(sound, 1f, 1f)
        } else {
            currentList.items.removeValue(currentList.selected, true)
        }
    }

    private val selectedSamples = mutableSetOf<ListItem.SoundFile>()

    private fun setUpCommands() {
        commandMap.setUp(Keys.TAB, "Switch lists") {
            switchList()
        }
        commandMap.setUp(Keys.LEFT, "Go Down a Note") {
            currentNote--
            if (currentNote < noteMin) {
                currentNote = noteMin
            }
        }
        commandMap.setUp(Keys.RIGHT, "Go Up a Note") {
            currentNote++
            if (currentNote > noteMax) {
                currentNote = noteMax
            }
        }

        commandMap.setUp(Keys.A, "Previous Sample") {
            allSamples.previousItem()
        }
        commandMap.setUp(Keys.D, "Next Sample") {
            allSamples.nextItem()
        }

        commandMap.setUp(Keys.W, "Play current sample") {
            tryToPlay(allSamples.selectedItem)
        }

        commandMap.setUp(Keys.Q, "Add current Sample") {
            selectedSamples.add(allSamples.selectedItem)
        }

        commandMap.setUp(Keys.PAGE_UP, "Go ten up") {
            goTenUpInCurrentList()
        }

        commandMap.setUp(Keys.PAGE_DOWN, "Go ten down") {
            goTenDownInCurrentList()
        }


        commandMap.setDown(Keys.UP, "Go up in dirlist") {
            goUpInCurrentList()
        }
        commandMap.setDown(Keys.DOWN, "Go up in dirlist") {
            goDownInCurrentList()
        }
        commandMap.setUp(Keys.ENTER, "Select ITem in dirlist") {
            selectCurrentItemInList()
        }
        commandMap.setUp(Keys.BACKSPACE, "Go back one directory or switch back to previous list") {
            if (currentListIsFileList) {
                switchList()
            }
            if (currentDirectory.parent != null) {
                currentDirectory = currentDirectory.parent!!
                updateFilesAndFoldersLists()
            }
        }
        commandMap.setUp(Keys.SPACE, "Select this sample") {
            if (currentListIsFileList) {
                selectedSamples.add(currentList.selected as ListItem.SoundFile)
                currentSamples.setItems(selectedSamples.toGdxArray())
                currentSamples.refreshItems()
            }
        }

        commandMap.setUp(Keys.S, "Save Instrument") {
            if (selectedSamples.any()) {
                saveInstrument()
            }
        }
    }

    private fun goTenDownInCurrentList() {
        if (currentList.selectedIndex == currentList.items.lastIndex)
            currentList.selectedIndex = currentList.items.lastIndex
        else if (currentList.selectedIndex - 10 < 0)
            currentList.selectedIndex = 0
        else
            currentList.selectedIndex -= 10
    }

    private fun goTenUpInCurrentList() {
        if (currentList.selectedIndex == 0)
            currentList.selectedIndex = 0
        else if (currentList.selectedIndex + 10 > currentList.items.lastIndex)
            currentList.selectedIndex = currentList.items.lastIndex
        else
            currentList.selectedIndex += 10
    }

    var instrumentCount = 0
    private fun saveInstrument() {
        val json = Json.encodeToString(selectedSamples)

        instrumentCount++
        Gdx.files.local("instrument-$instrumentCount.json").writeString(json, true)
        selectedSamples.clear()
    }

    fun updateFilesAndFoldersLists() {
        dirList.setItems(currentDirectory.childDirs.toGdxArray())
        fileList.setItems(currentDirectory.files.toGdxArray())
        if (currentDirectory.childDirs.isEmpty() && currentListIsDirList)
            switchList()
        dirList.refreshItems()
        fileList.refreshItems()
    }

    override fun show() {


        Scene2DSkin.defaultSkin = Skin(Gdx.files.internal("ui/uiskin.json"))
        super.show()
        if (::stage.isInitialized) {
            stage.clear()
            stage.dispose()
        }
        stage = getStage()
        setUpCommands()
    }

    override fun render(delta: Float) {
        super.render(delta)
        stage.act(delta)
        stage.draw()
    }


    private lateinit var stage: Stage
    private lateinit var dirList: KListWidget<ListItem>
    private lateinit var fileList: KListWidget<ListItem>
    private lateinit var currentList: KListWidget<ListItem>
    private lateinit var currentSamples: KListWidget<ListItem>
    private val shapeDrawer by lazy { inject<ShapeDrawer>() }
    fun get16th(timeBars: Float) = floor(timeBars * 16f) % 16

    private fun getStage(): Stage {
        return stage(batch, viewport).apply {
            actors {
                table {
                    table {
                        label("Sample Discovery") {
                            setFontScale(4f)
                            setAlignment(Align.center)
                        }.cell(expandX = true, fillX = true, align = Align.center, colspan = 4, padTop = 25f)
                        row()
                        boundLabel({allSamples.selectedItem.name})
                        row()
                        boundLabel({ currentNote.toString() })
                        row()
                        dirList = listWidgetOf(currentDirectory.childDirs.toGdxArray())
                        fileList = listWidgetOf(currentDirectory.files.toGdxArray())
                        row()
                        currentSamples = listWidgetOf(selectedSamples.toGdxArray())
                    }.align(Align.center or Align.top)
                    currentList = dirList
                    setFillParent(true)
                }
            }
        }
    }


}