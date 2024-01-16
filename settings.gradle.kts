include(":core")

include(":multisrc")
project(":multisrc").projectDir = File("multisrc")

File(rootDir, "lib").eachDir {
    include(":lib:${it.name}")
}

File(rootDir, "src").eachDir { dir ->
    dir.eachDir { subdir ->
        val name = ":extensions:individual:${dir.name}:${subdir.name}"
        include(name)
        project(name).projectDir = File("src/${dir.name}/${subdir.name}")
    }
}

File(rootDir, "generated-src").eachDir { dir ->
    dir.eachDir { subdir ->
        val name = ":extensions:multisrc:${dir.name}:${subdir.name}"
        include(name)
        project(name).projectDir = File("generated-src/${dir.name}/${subdir.name}")
    }
}

fun File.eachDir(block: (File) -> Unit) =
    listFiles()?.filter { it.isDirectory }?.forEach(block)
