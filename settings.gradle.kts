include(":core")

File(rootDir, "multisrc").eachDir { include(":multisrc:${it.name}") }

File(rootDir, "lib").eachDir { include(":lib:${it.name}") }

File(rootDir, "extensions").eachDir { dir ->
    dir.eachDir { include(":extensions:${dir.name}:${it.name}") }
}

rootProject.name = "tachiyomi-extensions"

fun File.eachDir(block: (File) -> Unit) =
    listFiles(File::isDirectory)?.forEach(block)
