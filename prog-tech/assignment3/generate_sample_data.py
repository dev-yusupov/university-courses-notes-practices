"""
Sample data generator for testing the Jupyter Notebook analysis
This creates mock data that simulates what would be scraped from the Springer journal
"""

import pandas as pd
import random
from datetime import datetime, timedelta

# Sample data to simulate scraping results
sample_titles = [
    "The impact of socioeconomic status on health outcomes in urban populations",
    "Nutritional patterns and their economic determinants in developing countries",
    "Long-term effects of childhood malnutrition on adult productivity",
    "Economic factors influencing healthcare access in rural communities",
    "The relationship between education and health behaviors across cultures",
    "Climate change impacts on agricultural productivity and nutrition",
    "Gender disparities in health outcomes: An economic perspective",
    "The role of public health interventions in economic development",
    "Analyzing the cost-effectiveness of preventive healthcare programs",
    "Economic consequences of obesity in high-income countries",
    "The effect of income inequality on population health metrics",
    "Historical trends in life expectancy and economic growth",
    "Maternal health and its impact on economic development",
    "The economics of vaccination programs in low-income regions",
    "Food security and economic stability: A longitudinal analysis",
    "Urban-rural health disparities in transition economies",
    "The impact of healthcare spending on mortality rates",
    "Economic analysis of chronic disease management programs",
    "The relationship between environmental quality and public health",
    "Labor market outcomes and health status: A causal analysis",
]

sample_authors = [
    "Smith, J.", "Johnson, M.", "Williams, R.", "Brown, A.", "Jones, K.",
    "Garcia, L.", "Miller, T.", "Davis, S.", "Rodriguez, C.", "Martinez, P.",
    "Hernandez, D.", "Lopez, N.", "Gonzalez, F.", "Wilson, E.", "Anderson, H.",
    "Thomas, B.", "Taylor, W.", "Moore, V.", "Jackson, G.", "Martin, I."
]

paper_types = [
    "Research Article",
    "Research Article",
    "Research Article",
    "Research Article",
    "Research Article",
    "Research Article",
    "Review Article",
    "Original Paper",
    "Original Paper",
    "Editorial"
]

def generate_sample_data(n_articles=50):
    """Generate sample data similar to what would be scraped"""
    data = []
    
    base_date = datetime(2020, 1, 1)
    
    for i in range(n_articles):
        # Random date within the last 4 years
        days_offset = random.randint(0, 1460)
        pub_date = base_date + timedelta(days=days_offset)
        
        # Random number of authors (1-5)
        n_authors = random.randint(1, 5)
        authors = random.sample(sample_authors, n_authors)
        
        article = {
            'title': random.choice(sample_titles),
            'authors': ', '.join(authors),
            'paper_type': random.choice(paper_types),
            'publication_date': pub_date.strftime('%d %B %Y')
        }
        data.append(article)
    
    return data

if __name__ == "__main__":
    # Generate sample data
    sample_data = generate_sample_data(50)
    
    # Create DataFrame
    df = pd.DataFrame(sample_data)
    
    # Save to CSV
    df.to_csv('springer_journal_articles_sample.csv', index=False)
    print(f"✓ Generated {len(df)} sample articles")
    print(f"✓ Saved to springer_journal_articles_sample.csv")
    print("\nFirst 5 articles:")
    print(df.head())
