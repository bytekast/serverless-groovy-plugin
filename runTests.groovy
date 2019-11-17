final pattern1 = new FileNameFinder().getFileNames(args ? args[0] : '.', '**/*Test.groovy')
final pattern2 = new FileNameFinder().getFileNames(args ? args[0] : '.', '**/*test.groovy')
(pattern1 + pattern2).each { file ->
  println "Running test: $file"
  def proc = "groovy $file".execute()
  println proc.in.text
  if (proc.exitValue() != 0) {
    println proc.err.text
  }
}
