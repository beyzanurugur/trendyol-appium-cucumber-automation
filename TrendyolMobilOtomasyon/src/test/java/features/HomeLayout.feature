@homeLayout
Feature: Home Layout Page

  Scenario: viewing notifications
    When user clicks "notifications button"
    When user goes back to the previous page
    #When user goes back to the previous page
    And user clicks "home button"
    Then verify user is on the "home layout page"


  Scenario: adding product to cart and then removing it from cart
    When user clicks "first product in the page"
    And user clicks "sepete ekle button"
    #sepete eklediğimiz ürün için beden seçeneği varsa aşağıdaki iki satırı yorum satırı olmaktan çıkarın
    #And user selects the "third" item in the "size list uiautomator"
    #And user clicks "2.sepete ekle button"
    And user clicks "first delete button"
    And user clicks "second delete button"
    And user clicks "home button"
    When user goes back to the previous page
    Then verify user is on the "home layout page"


  Scenario: scroll down in the product page
    Given verify user is on the "home layout page"
    When user clicks "first product in the page"
    When user scrolls down the page "80" percent
    When user scrolls down the page "80" percent
    When user goes back to the previous page
    Then verify user is on the "home layout page"


  Scenario: scroll to the specific element
    Given verify user is on the "home layout page"
    When user clicks "first product in the page"
    When user scrolls until they see the "geri bildirim ver"
    Then verify user is on the "geri bildirim ver"


  Scenario: verify q&a number
    Given verify user is on the "home layout page"
    When user clicks "first product in the page"
    When user scrolls until they see the "ürün soru cevap"
    And user clicks "ürün soru cevap"
    #aşağıdaki soru sayısını elle girdik ancak dinamik olarak da alabiliriz, nasıl yapılacağını ilgili metodun içine yorum satırı olarak düştüm
    When verify total question number is equal to 29


