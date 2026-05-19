# PEPPOL BOX TRANSMITTER

A small Kotlin CLI that reads PEPPOL XML invoices from an input folder and sends them to the PEPPOL network via the
Peppol Box API, then moves successfully transmitted files to an output folder.

---

## Technologies

- Kotlin 2.3.20 (JVM target 24)
- Maven (build, test, packaging)
- OpenAPI Generator 7.17.0 (API client code)
	- OkHttp 5.3.1 (HTTP client)
	- Moshi 1.15.2 (JSON)
- Valiktor 0.12.0 (validation)
- JUnit Jupiter 6.x + MockK 1.14.6 (testing)

## Prerequisites (for development)

- Java 24 JDK (if you run from source; the packaged ZIP ships its own JRE)
- Maven 3.8+

## Project layout (high level)

- Entry point: `be.compuwave.peppol_box_transmitter.MainKt`
- CLI argument parser: ProgramArguments (expects --properties=...)
- Configuration model: ConfigModel loaded via PropertyParser from .properties file
- File utilities: reads XML files from INPUT_DIRECTORY and moves them after successful transmission
- `Transmitter`:
	- `Sender`: sends given documents to the Peppol Box API
	- `Downloader`: download documents from a specific date from Peppol Box API

## Configuration (.properties)

Create a properties file and pass its path when running the app. Keys:

| Property             | Description                                                                            | Default                       | Used during  |
|:---------------------|:---------------------------------------------------------------------------------------|:------------------------------|:------------:|
| `TEST_MODE`          | If `true`, uses the API in test mode                                                   |                               |   ⬇️   ⬆️    |
| `INPUT_DIRECTORY`    | Path to the directory containing XML invoice files to send                             |                               |      ⬆️      |
| `OUTPUT_DIRECTORY`   | Path to the directory where successfully sent files will be moved (created if missing) | `${INPUT_DIRECTORY}/sent`     |      ⬆️      |
| `LOGGING_DIRECTORY`  | Path to the directory where logging file will be saved (created if missing)            | `${current working dir}/logs` |  ⬇️     ⬆️   |
| `DOWNLOAD_FROM`      | The date from which to download documents (format: `yyyy-MM-dd HH:mm:ss`)              | `2026-01-01 00:00:00`         |      ⬇️      |
| `DOWNLOAD_DIRECTORY` | Path to the directory where downloaded files will be saved (created if missing)        |                               |      ⬇️      |
| `BASE_URL`           | Base URL of the Peppol Box API (e.g., `https://www.peppol-box.be/portal/`)             |                               |   ⬇️   ⬆️    |
| `TENANT_ID`          | Your tenant identifier                                                                 |                               |   ⬇️   ⬆️    |
| `API_KEY`            | Your API key                                                                           |                               |   ⬇️   ⬆️    |
| `API_SECRET`         | Your API secret                                                                        |                               |   ⬇️   ⬆️    |

- ⬇️ downloading invoice
- ⬆️ sending invoice

Example:

```properties
TEST_MODE=true
INPUT_DIRECTORY=src/main/resources/examples
OUTPUT_DIRECTORY=src/main/resources/examples/sent
LOGGING_DIRECTORY=src/main/resources/logs
DOWNLOAD_FROM=2026-01-01 00:00:00
DOWNLOAD_DIRECTORY=src/main/resources/downloads
BASE_URL=https://www.peppol-box.be/portal/
TENANT_ID=your-tenant
API_KEY=your-api-key
API_SECRET=your-api-secret
```

⚠️ For Windows, the `\` should be escaped

```properties
TEST_MODE=true
INPUT_DIRECTORY=C:\\path\\to\\directory
OUTPUT_DIRECTORY=C:\\path\\to\\directory\\sent
LOGGING_DIRECTORY=C:\\path\\to\\log
DOWNLOAD_FROM=2026-01-01 00:00:00
DOWNLOAD_DIRECTORY=C:\\path\\to\\downloads
BASE_URL=https://www.peppol-box.be/portal/
TENANT_ID=your-tenant
API_KEY=your-api-key
API_SECRET=your-api-secret
```

## Develop: build, test, run

- Build (with tests):
  ```bash
  mvn clean package
  ```
- Run tests only:
  ```bash
  mvn test
  ```
- Run locally from the fat JAR (after package):
  ```bash
  java -jar target/peppol-box-transmitter-1.0.0-SNAPSHOT-jar-with-dependencies.jar --properties=path/to/file.properties
  ```
  Replace the version in the JAR name if it changes.

## Package a distributable ZIP

The project assembles a cross-platform ZIP that bundles platform-specific JREs and helper scripts.

- Create the ZIP:
  ```bash
  mvn -DskipTests package
  ```
- Output artifacts:
	- Fat JAR: `target/peppol-box-transmitter-<version>-jar-with-dependencies.jar`
	- ZIP distribution: `target/peppol-box-transmitter-<version>-zip.zip`
	  (Some tools may also show an extracted folder named peppol-box-transmitter-<version>-zip/.)

## Use the generated ZIP (recommended for end users)

1) Unzip `target/peppol-box-transmitter-<version>-zip.zip` to a folder of your choice.
2) Inside the unzipped folder you will find:
	- The fat JAR: peppol-box-transmitter-<version>-jar-with-dependencies.jar
	- Platform JRE folders (e.g., zulu21...-linux_x64, ...-macosx_x64, ...-macosx_aarch64, ...-win_x64)
	- Run scripts:
		- Windows: `run-sender|downloader--win_x64.bat`
		- Linux: `run-sender|downloader--linux_x64`
		- macOS (Intel): `run-sender|downloader--macosx_x64`
		- macOS (Apple Silicon): `run-sender|downloader--macosx_aarch64`

3) Run the script for your OS, passing the path to your .properties file:

- Windows (x64):
  ```bat
  run-sender|downloader--win_x64.bat path\to\file.properties
  ```
- Linux (x64):
  ```bash
  ./run-sender|downloader--linux_x64 path/to/file.properties
  ```
- macOS (Apple Silicon):
  ```bash
  ./run-sender|downloader--macosx_aarch64 path/to/file.properties
  ```

Notes:

- On Linux/macOS you may need to allow execute permission once:
  ```bash
  chmod +x run-sender|downloader--*
  ```
- The scripts automatically select the bundled JRE and launch the fat JAR.
- To send documents, ensure the INPUT_DIRECTORY exists and contains .xml files; successful files are moved to
  OUTPUT_DIRECTORY.

## Troubleshooting

- Fat JAR not found next to the script: make sure you unzipped the distribution completely and are running the script
  from the unzipped folder.
- Bundled JRE not found: ensure you didn’t remove or move the versioned JRE folder that sits alongside the script.
- macOS “cannot be opened because it is from an unidentified developer”: you may need to allow the script in System
  Settings or run `xattr -dr com.apple.quarantine <unzipped-folder>` at your own risk/policy.
- Invalid argument errors: the app expects `--properties=...` when running the JAR directly, or a single
  `path/to/file.properties` argument when using the provided scripts.

Made with love by CompuWave 🧡
README and GitHub Action template generated by Junie© 
