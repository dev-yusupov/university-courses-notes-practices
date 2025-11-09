#!/usr/bin/env python3
"""
Test script for the Springer journal scraper
This verifies that the scraping functions work correctly
"""

import requests
from bs4 import BeautifulSoup
import pandas as pd
import re
import time

def fetch_page(url):
    """Fetch the content of a web page."""
    try:
        headers = {
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36'
        }
        response = requests.get(url, headers=headers, timeout=10)
        response.raise_for_status()
        soup = BeautifulSoup(response.content, 'html.parser')
        return soup
    except requests.exceptions.RequestException as e:
        print(f"Error fetching {url}: {e}")
        return None

def extract_article_data(article_element):
    """Extract data from a single article element."""
    article_data = {
        'title': None,
        'authors': None,
        'paper_type': None,
        'publication_date': None
    }
    
    try:
        # Extract title
        title_elem = article_element.find('a', {'class': 'c-card__link'})
        if not title_elem:
            title_elem = article_element.find('h3', {'class': 'c-card__title'})
        if title_elem:
            article_data['title'] = title_elem.get_text(strip=True)
        
        # Extract authors
        authors_elem = article_element.find('ul', {'class': 'c-author-list'})
        if not authors_elem:
            authors_elem = article_element.find('span', {'class': 'c-author-list'})
        if authors_elem:
            authors = [author.get_text(strip=True) for author in authors_elem.find_all('li')]
            if not authors:
                authors_text = authors_elem.get_text(strip=True)
                authors = [a.strip() for a in authors_text.split(',')]
            article_data['authors'] = ', '.join(authors) if authors else None
        
        # Extract paper type
        type_elem = article_element.find('span', {'class': 'c-meta__type'})
        if not type_elem:
            type_elem = article_element.find('span', string=re.compile('Article|Review|Editorial|Letter', re.I))
        if type_elem:
            article_data['paper_type'] = type_elem.get_text(strip=True)
        
        # Extract publication date
        date_elem = article_element.find('time')
        if not date_elem:
            date_elem = article_element.find('span', {'class': 'c-meta__item'})
        if date_elem:
            date_text = date_elem.get_text(strip=True)
            article_data['publication_date'] = date_text
    
    except Exception as e:
        print(f"Error extracting article data: {e}")
    
    return article_data

def test_scraper():
    """Test the scraper with a single page"""
    print("Testing Springer Journal Scraper...")
    print("=" * 60)
    
    # Test URL
    url = "https://link.springer.com/journal/12134/articles"
    print(f"\nFetching: {url}")
    
    # Fetch the page
    soup = fetch_page(url)
    
    if soup is None:
        print("❌ Failed to fetch the page")
        return False
    
    print("✓ Successfully fetched the page")
    
    # Find article elements
    articles = soup.find_all('article', {'class': 'c-card'})
    if not articles:
        articles = soup.find_all('div', {'class': 'app-article-list-row__item'})
    
    print(f"✓ Found {len(articles)} article elements")
    
    if len(articles) == 0:
        print("❌ No articles found - website structure may have changed")
        return False
    
    # Test extraction on first few articles
    print("\nTesting data extraction on first 3 articles:")
    print("-" * 60)
    
    extracted_data = []
    for i, article in enumerate(articles[:3]):
        data = extract_article_data(article)
        extracted_data.append(data)
        
        print(f"\nArticle {i+1}:")
        print(f"  Title: {data['title'][:60] if data['title'] else 'NOT FOUND'}...")
        print(f"  Authors: {data['authors'][:60] if data['authors'] else 'NOT FOUND'}...")
        print(f"  Type: {data['paper_type'] if data['paper_type'] else 'NOT FOUND'}")
        print(f"  Date: {data['publication_date'] if data['publication_date'] else 'NOT FOUND'}")
    
    # Check if we got at least some data
    titles_found = sum(1 for d in extracted_data if d['title'])
    authors_found = sum(1 for d in extracted_data if d['authors'])
    types_found = sum(1 for d in extracted_data if d['paper_type'])
    dates_found = sum(1 for d in extracted_data if d['publication_date'])
    
    print("\n" + "=" * 60)
    print("EXTRACTION SUCCESS RATE:")
    print(f"  Titles: {titles_found}/3 ({titles_found/3*100:.1f}%)")
    print(f"  Authors: {authors_found}/3 ({authors_found/3*100:.1f}%)")
    print(f"  Types: {types_found}/3 ({types_found/3*100:.1f}%)")
    print(f"  Dates: {dates_found}/3 ({dates_found/3*100:.1f}%)")
    
    if titles_found >= 2:
        print("\n✓ TEST PASSED - Scraper is working!")
        return True
    else:
        print("\n❌ TEST FAILED - Not enough data extracted")
        return False

if __name__ == "__main__":
    success = test_scraper()
    exit(0 if success else 1)
