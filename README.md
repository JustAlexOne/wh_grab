# Warhammer Underworlds Card Grabber

A Java/Groovy-based tool for downloading, processing, and generating print-and-play (PnP) PDFs for Warhammer Underworlds card game cards.

## Overview

This project provides utilities to:
- Fetch card data from the official Warhammer Underworlds website API
- Download card images for various sets and warbands
- Process and clean up card images
- Generate print-and-play PDF files with proper formatting and card backs
- Handle different card types (objectives, upgrades, ploys, gambit spells)

## Features

- **Card Data Fetching**: Retrieves card information from the Warhammer Underworlds WordPress API
- **Image Download**: Automatically downloads card images from the official website
- **Image Processing**: Tools for cleaning and processing card images (color removal, border addition)
- **PDF Generation**: Creates print-ready PDF files with:
  - Configurable card layout (rows/columns)
  - Card backs (objective and power cards)
  - Proper margins and spacing for printing
- **Card Filtering**: Filter cards by type, warband, and set

## Prerequisites

- Java 8 or higher
- Maven 3.x
- Internet connection (for downloading card data and images)

## Dependencies

Key dependencies include:
- **iTextPDF 5.5.10**: PDF generation and manipulation
- **Apache PDFBox 2.0.19**: Additional PDF processing
- **Unirest 1.4.9**: HTTP requests for API calls
- **Jackson 2.10.4**: JSON parsing
- **Lombok 1.16.16**: Boilerplate code reduction
- **Joda-Time 2.9.3**: Date/time handling

See `pom.xml` for complete dependency list.

## Installation

1. Clone the repository:
```bash
git clone https://github.com/JustAlexOne/wh_grab.git
cd wh_grab
```

2. Build the project using Maven:
```bash
mvn clean install
```

## Usage

### Fetching Card Data

The `Grabber` class fetches card data from the Warhammer Underworlds API:

```java
// Main grabber - fetches cards from API or local file
java -cp target/wh_grab-1.0-SNAPSHOT.jar com.justalex.grabber.Grabber
```

The grabber can:
- Fetch data directly from the API
- Read from cached local files (e.g., `a_814_cards.txt`)
- Find specific cards by card number

### Generating Print-and-Play PDFs

The Groovy scripts provide functionality for creating PnP PDFs:

```groovy
// Example: Generate PDFs for objective cards
// See GroovyGrabber.groovy for implementation details
```

### Image Processing

Use the `ImageReader` class to clean up and process card images:

```java
// Processes images by removing unwanted colors and keeping specific color ranges
java -cp target/wh_grab-1.0-SNAPSHOT.jar com.justalex.imageCleanup.ImageReader
```

## Project Structure

```
wh_grab/
├── src/
│   └── main/
│       ├── java/com/justalex/
│       │   ├── grabber/          # Card data fetching and parsing
│       │   │   ├── Grabber.java  # Main API fetcher
│       │   │   ├── CustomPojoMapper.java
│       │   │   └── pojos/        # Data model objects
│       │   ├── imageCleanup/     # Image processing utilities
│       │   │   └── ImageReader.java
│       │   └── pnp/              # Print-and-play generation
│       │       ├── CardType.java
│       │       ├── GroovyGrabber.groovy  # Main PnP generator
│       │       ├── HttpWorker.groovy     # Image downloading
│       │       ├── ImageWorker.groovy    # Image manipulation
│       │       ├── PdfWorker.groovy      # PDF creation
│       │       └── PnpWorker.groovy      # Data file reading
│       └── resources/
│           └── data/
│               ├── card_backs/   # Card back images
│               ├── sets_en.json  # Set information
│               └── warbands_en.json  # Warband information
├── pom.xml                       # Maven configuration
└── *.txt                         # Cached card data files
```

## Card Types

The tool supports all Warhammer Underworlds card types:
- **Objective**: Scoring cards
- **Upgrade**: Fighter upgrade cards
- **Ploy**: Instant effect cards
- **Gambit Spell**: Spell cards

## Data Sources

- **Card API**: `https://warhammerunderworlds.com/wp-json/wp/v2/cards/`
- **Card Images**: Downloaded from official Warhammer Underworlds website
- **Local Cache**: Card data can be cached locally in text files for offline use

## Output

Generated PDFs are formatted for printing with:
- A3 landscape orientation (1191x842)
- Configurable margins
- Card backs on reverse pages
- Multiple cards per page for efficient printing

## Technologies Used

- **Java 8**: Core programming language
- **Groovy**: Scripting for complex workflows
- **Maven**: Build and dependency management
- **iText**: PDF generation
- **PDFBox**: PDF manipulation
- **Jackson**: JSON processing
- **Unirest**: HTTP client

## Notes

- Card images and data are copyrighted by Games Workshop
- This tool is for personal use only
- Downloaded images are saved to the `card_images/` directory (gitignored)
- Sample data files (a_2_cards.txt, a_10_cards.txt, etc.) contain cached API responses

## License

This is a personal project. Warhammer Underworlds and all related content are property of Games Workshop Ltd.

## Contributing

This appears to be a personal utility project. For questions or issues, please contact the repository owner.
