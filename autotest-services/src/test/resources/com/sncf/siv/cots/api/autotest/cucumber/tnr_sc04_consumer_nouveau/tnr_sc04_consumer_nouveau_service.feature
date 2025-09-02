@TNR_SC04
Feature: Consumer nouveau service - Tests de non-régression (TNR)
  En tant que système COTS
  Je veux vérifier le bon fonctionnement du consumer nouveau service

  Background:
    Given les données de test TNR_SC04 sont nettoyées

  @TNR_SC04_T01
  Scenario: Recherche de etatCourseObservee - Par idImmuable (TNR-SC04-T01)
    Given l'etatCourseObservee avec idImmuable "2148:CU22SC02T01-F-1187-2025-12-31" n'existe pas en base
    And json contenu dans le corps de la requete externe "testdata/tnr_sc04_consumer_nouveau/json/request/SC04_T01_Prerequis_importerEtatCourseObservee.json"
    When Requete HTTP externe methode "POST" URL "/import/importerEtatCourseObservee"
    Then Code HTTP externe 200 recu
    And l'etatCourseObservee avec idImmuable "2148:CU22SC02T01-F-1187-2025-12-31" doit être présent en base
    When Requete HTTP consumer GET URL "/etatsCoursesObservees" avec parametre idImmuable "2148:CU22SC02T01-F-1187-2025-12-31"
    Then Code HTTP consumer 200 recu
    And un seul objet est retourné dans la réponse consumer
    And la réponse consumer contient le numeroCourse "CU22SC02T01"
    And la réponse consumer correspond au modèle attendu "testdata/tnr_sc04_consumer_nouveau/json/response/SC04_T01_Reponse_GET_etatsCoursesObservees.json"

  @TNR_SC04_T02
  Scenario: Rechercher un etatCourseDistribuable par idImmuable (TNR-SC04-T02)
    Given l'ECD avec idImmuable "2148:1187-F-835023-20251228" n'existe pas en base
    And json contenu dans le corps de la requete externe "testdata/tnr_sc04_consumer_nouveau/json/request/SC04_T02_Prerequis_ImporterEtatCourseDistribuable_PTA.json"
    When Requete HTTP externe methode "POST" URL "/import/importerEtatsCoursesDistribuables"
    Then Code HTTP externe 200 recu
    And l'ECD avec idImmuable "2148:1187-F-835023-20251228" doit être présent en base
    When Requete HTTP consumer GET URL "/etatsCoursesDistribuables" avec parametre idImmuable "2148:1187-F-835023-20251228"
    Then Code HTTP consumer 200 recu
    And un seul objet est retourné dans la réponse consumer
    And la réponse consumer contient le numeroCourse "835023"
    And la réponse consumer correspond au modèle attendu "testdata/tnr_sc04_consumer_nouveau/json/response/SC04_T02_Reponse_GET_etatsCoursesDistribuables.json" en excluant les champs dynamiques

  @TNR_SC04_T03
  Scenario: Récupérer un ou plusieurs dossiers événements - Recherche sur un codeTransporteurResponsable donné (TNR-SC04-T03)
    Given le dossier événement avec identifiantDansSystemeCreateur "5D-2025-09-15" n'existe pas en base
    And json contenu dans le corps de la requete externe "testdata/tnr_sc04_consumer_nouveau/json/request/SC04_T03_Prerequis_Dossier_Evenement.json"
    When Requete HTTP externe methode "POST" URL "/import/importerDossierEvenement"
    Then Code HTTP externe 200 recu
    And le dossier événement avec identifiantDansSystemeCreateur "5D-2025-09-15" doit être présent en base
    When Requete HTTP consumer GET URL "/dossierEvenements" avec parametres identifiantDansSystemeCreateur "5D-2025-09-15" et systemeCreateur "2148"
    Then Code HTTP consumer 200 recu
    And un seul objet est retourné dans la réponse consumer
    And la réponse consumer contient "5D-2025-09-15"
    And la réponse consumer correspond au modèle attendu "testdata/tnr_sc04_consumer_nouveau/json/response/SC04_T03_Reponse_GET_dossierEvenements.json" en excluant les champs dynamiques

  @TNR_SC04_T04
  Scenario: Recherche de etatCourseProductible - Par idImmuable (TNR-SC04-T04)
    Given l'ECP avec idImmuable "2148:1187-F-2811-20251207" n'existe pas en base
    And json contenu dans le corps de la requete externe "testdata/tnr_sc04_consumer_nouveau/json/request/SC04_T04_Prerequis_ImporterEtatCourseProductible_PTA_Passe_Minuit.json"
    When Requete HTTP externe methode "POST" URL "/import/importerEtatsCoursesProductibles"
    Then Code HTTP externe 200 recu
    And l'ECP avec idImmuable "2148:1187-F-2811-20251207" doit être présent en base
    When Requete HTTP consumer GET URL "/etatsCoursesProductibles" avec parametre idImmuable "2148:1187-F-2811-20251207"
    Then Code HTTP consumer 200 recu
    And un seul objet est retourné dans la réponse consumer
    And la réponse consumer contient le numeroCourse "2811"
    And la réponse consumer ne contient pas le champ "courseCoupleeEstAcheminee"
    And la réponse consumer correspond au modèle attendu "testdata/tnr_sc04_consumer_nouveau/json/response/SC04_T04_Reponse_GET_etatsCoursesProductibles.json" en excluant les champs dynamiques