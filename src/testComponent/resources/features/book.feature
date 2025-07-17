Feature: the user can create reserve and retrieve the books
  Scenario: user creates two books and retrieve both of them
    When the user creates the book "Les Misérables" written by "Victor Hugo"
    And the user creates the book "L'avare" written by "Molière"
    And the user get all books
    Then the list should contains the following books in the same order
      | title | author | reserved |
      | L'avare | Molière | false |
      | Les Misérables | Victor Hugo | false |

  Scenario: the user reserve the first book and retrieve both of them
    When the user creates the book "Les Misérables" written by "Victor Hugo"
    And the user creates the book "L'avare" written by "Molière"
    And the user reserve the book "Les Misérables"
    And the user get all books
    Then the list should contains the following books in the same order
      | title | author | reserved |
      | L'avare | Molière | false |
      | Les Misérables | Victor Hugo | true |

