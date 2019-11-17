final pattern1 = new FileNameFinder().getFileNames(args ? args[0] : '.', '**/*Test.groovy', '**/node_modules/**')
final pattern2 = new FileNameFinder().getFileNames(args ? args[0] : '.', '**/*test.groovy', '**/node_modules/**')
(pattern1 + pattern2).each { file ->
  println "Running test: $file"
  def proc = "groovy $file".execute()
  println proc.in.text
  proc.waitFor()
  if (proc.exitValue() != 0) {
    println proc.err.text
  }
}
