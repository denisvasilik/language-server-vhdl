# VHDL Language Server

The VHDL language server implements the [Language Server Protocol]. 

Its primary clients are [VHDL extension for VS Code] and [VHDL extension for Theia IDE].

## Build

Gradle is used to build the language server.

```bash
~$ ./gradlew build
```

The build output can be found in the following directory.

```bash
com.eccelerators.plugins.vhdl.ide/build/distributions/language-server.zip
```

# License

This project is made available under the MIT License.

[XText]:https://www.eclipse.org/Xtext/
[Language Server Protocol]:https://langserver.org/
[VHDL extension for VS Code]:https://github.com/denisvasilik/vscode-vhdl
[VHDL extension for Theia IDE]:https://github.com/denisvasilik/vscode-vhdl