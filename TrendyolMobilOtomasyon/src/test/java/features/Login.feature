Feature: Login Page

  Scenario: failed sign up
    Given verify user is on the "home layout page"
    When user clicks "hesabim button"
    #When user clicks "giris yap button"
    And user clicks "üye ol"
    And user enters "aaa" on "email textbox"
    And user enters "aaa" on "password textbox"
    And user clicks "acik riza check button"
    And user clicks "aydinlatma metni check button"
    And user clicks "üye ol button"
    Then verify user is on the "email error text"


