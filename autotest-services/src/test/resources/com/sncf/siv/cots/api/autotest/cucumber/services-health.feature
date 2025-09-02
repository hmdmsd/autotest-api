Feature: Services Health Check - Tests de santé des services COTS

  En tant que système d'automatisation de tests
  Je veux vérifier l'état de santé des services COTS
  Afin de m'assurer que tous les services sont opérationnels

  Scenario: Vérifier l'état de santé de tous les services
    When Requete HTTP methode "GET" URL "/api/services/health"
    Then Code HTTP 200 recu
    And la réponse contient des données de santé

  Scenario: Vérifier l'état de santé d'un service spécifique - autotest-api
    When Requete HTTP methode "GET" URL "/api/services/health/autotest-api"
    Then Code HTTP 200 recu
    And la réponse contient le service "autotest-api"