# Assignment 3: Web Scraping and Data Analysis

## Overview

This assignment focuses on collecting data from the internet and using it to create a structured database suitable for analysis. The project includes web scraping, data processing, visualization, and analysis.

## Project Description

**Website**: [Springer Journal - Economics and Human Biology](https://link.springer.com/journal/12134/articles)

**Objective**: Collect and analyze academic article data including:
- Article titles
- Authors
- Paper types (Research Article, Review, etc.)
- Publication dates

## Files in This Assignment

1. **springer_journal_scraper.ipynb** - Main Jupyter Notebook containing:
   - Complete web scraping code with explanations
   - Data collection and database creation
   - Data analysis and visualizations
   - Comprehensive documentation of methodology

2. **requirements.txt** - Python dependencies needed to run the project

3. **Generated Files** (created when running the notebook):
   - `springer_journal_articles.csv` - Raw collected data
   - `springer_journal_articles_cleaned.csv` - Processed data
   - `springer_journal_articles.xlsx` - Excel format (optional)
   - `analysis_summary.txt` - Summary of findings

## Installation and Setup

1. Install required dependencies:
```bash
pip install -r requirements.txt
```

2. Launch Jupyter Notebook:
```bash
jupyter notebook springer_journal_scraper.ipynb
```

3. Run all cells in order to:
   - Scrape data from the Springer journal
   - Process and clean the data
   - Generate visualizations
   - Export results

## Features

### Data Collection
- Automated web scraping from Springer journal
- Extraction of titles, authors, paper types, and dates
- Respectful scraping with delays between requests
- Error handling for robust operation

### Data Analysis
- Summary statistics of collected data
- Distribution analysis of paper types
- Author collaboration patterns
- Publication trends over time

### Visualizations
- Pie chart of paper type distribution
- Histogram of author counts
- Bar chart of most prolific authors
- Time series of publications (if date data available)

## Usage

The notebook is fully commented and can be run cell by cell. Each section includes:
- Clear explanations of what the code does
- Purpose and context for each step
- Visualizations to illustrate findings

## Technical Details

- **Language**: Python 3
- **Main Libraries**: 
  - requests (web scraping)
  - BeautifulSoup4 (HTML parsing)
  - pandas (data manipulation)
  - matplotlib & seaborn (visualization)

## Academic Context

This project demonstrates:
1. Practical web scraping skills
2. Data processing and cleaning techniques
3. Statistical analysis of collected data
4. Data visualization for insights
5. Professional documentation practices

## Notes

- The scraper includes user-agent headers and delays to be respectful to the server
- Data is saved in multiple formats for flexibility
- The code is modular and can be adapted for other Springer journals
- All code includes comprehensive comments and explanations

## License

This is an academic project for educational purposes.
