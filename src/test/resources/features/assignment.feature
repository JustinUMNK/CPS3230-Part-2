Feature: Ability to find and search for categories

  Scenario Outline: Reachability of product categories (Check at least 5 categories)
    Given I am a user of the website
    When I visit the website
    And I click on the "<category>" category
    Then I should be taken to "<category>" category
    And the category should show at least 10 products
    When I click on the first product in the results
    Then I should be taken to the details page for that product

    Examples:
      | category               |
      | Laptops                |
      | Smartphones & Mobile   |
      | Televisions            |
      | Electronics            |
      | Gaming Monitors        |
      | Headphones & Earphones |
      | Apples                 |


  Scenario Outline: Search functionality
    Given I am a user of the website
    When I visit the website
    And I search for a product using the term "<searchTerm>"
    Then I should see the search results
    And there should be at least 5 products in the search results
    When I click on the first product in the results
    Then I should be taken to the details page for that product


    Examples:
      | searchTerm   |
      | Laptop       |
      | Mouse        |
      | Controller   |
      | Game         |
      | Phone        |
      | Bananas      |