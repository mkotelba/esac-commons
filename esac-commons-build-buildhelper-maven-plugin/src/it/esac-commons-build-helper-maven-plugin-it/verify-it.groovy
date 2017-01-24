import org.assertj.core.api.Assertions

Assertions.assertThat(new File(testMkdirsDirectory)).isDirectory()
Assertions.assertThat(new File(testMkdirsChildDirectoryPath1)).isDirectory()
Assertions.assertThat(new File(testMkdirsDirectory, testMkdirsChildDirectoryPath2)).isDirectory()

return true
