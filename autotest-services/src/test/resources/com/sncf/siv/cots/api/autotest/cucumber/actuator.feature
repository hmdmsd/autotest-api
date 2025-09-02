Feature: Actuator

  Scenario: Health est accessible libre
    When Requete HTTP methode "GET" URL "/management/health"
    Then Code HTTP 200 recu

  Scenario: Info est accessible sans authent
    When Requete HTTP methode "GET" URL "/management/info"
    Then Code HTTP 200 recu

  Scenario: les autres endpoint actuators necessitent une authentification
    When Requete HTTP methode "GET" URL "/management/loggers"
    Then Code HTTP 401 recu
