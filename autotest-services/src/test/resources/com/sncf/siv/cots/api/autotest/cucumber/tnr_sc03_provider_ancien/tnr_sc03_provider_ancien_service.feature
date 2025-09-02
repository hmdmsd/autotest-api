@TNR_SC03
Feature: Provider ancien service - Tests de non-régression (TNR)
  En tant que système COTS
  Je veux vérifier le bon fonctionnement du provider ancien service

  Background:
    Given les données de test TNR_SC03 sont nettoyées

  @TNR_SC03_T01
  Scenario: Importer une course cas simple (PTA) (TNR-SC03-T01)
    Given la course avec idImmuable "2148:1187-F-8080-20251202" n'existe pas en base
    And json contenu dans le corps de la requete externe "testdata/tnr_sc03_provider_ancien/json/request/SC03_T01_courseSimple_PTA.json"
    When Requete HTTP externe methode "POST" URL "/import/importerCourses"
    Then Code HTTP externe 200 recu
    And la réponse externe contient "coursesCreees"
    And la réponse externe contient le numeroCourse "8080"
    And la liste externe "coursesModifiees" doit être vide
    And la liste externe "coursesRejetees" doit être vide
    And la course avec idImmuable "2148:1187-F-8080-20251202" doit être présente en base

  @TNR_SC03_T02
  Scenario: Import en création Journée PTP simple avec sa course PTP (TNR-SC03-T02)
    Given le PTP avec dateApplication "2025-12-02" et codeTransporteurResponsable "5G" n'existe pas en base
    And json contenu dans le corps de la requete externe "testdata/tnr_sc03_provider_ancien/json/request/SC03_T02_importerPlansTransportPerturbe_PTP.json"
    When Requete HTTP externe methode "POST" URL "/import/importerPlansTransportPerturbe"
    Then Code HTTP externe 200 recu
    And la réponse externe contient "coursesOK"
    And la réponse externe contient "statutImport"
    And la course PTP avec idImmuable "2148:1187-F-140402-20251202" doit être présente en base
    And le PTP avec dateApplication "2025-12-02" et codeTransporteurResponsable "5G" doit être présent en base

  @TNR_SC03_T03
  Scenario: Service décommissionné - validerPlanTransportPerturbe (TNR-SC03-T03)
    Given json contenu dans le corps de la requete externe "testdata/tnr_sc03_provider_ancien/json/request/SC03_T03_validerPlansTransportPerturbe.json"
    When Requete HTTP externe methode "POST" URL "/import/validerPlanTransportPerturbe"
    Then Code HTTP externe 404 recu
    And la réponse externe contient "Not Found"

  @TNR_SC03_T04
  Scenario: Importer une course OPE cas simple (TNR-SC03-T04)
    Given la course OPE avec idImmuable "2148:1187-F-11998-20251129" n'existe pas en base
    And json contenu dans le corps de la requete externe "testdata/tnr_sc03_provider_ancien/json/request/SC03_T04_course_OPE.json"
    When Requete HTTP externe methode "POST" URL "/supervision/importerCourseOPE"
    Then Code HTTP externe 200 recu
    And la course OPE avec idImmuable "2148:1187-F-11998-20251129" doit être présente en base

  @TNR_SC03_T05
  Scenario: Créer implicitement une OPE au format etatCourseProductible - A partir d'une course PTA (TNR-SC03-T05)
    Given la course avec idImmuable "2148:1187-F-8824-20251110" n'existe pas en base
    And json contenu dans le corps de la requete externe "testdata/tnr_sc03_provider_ancien/json/request/SC03_T05_Prerequis_CoursePTA.json"
    When Requete HTTP externe methode "POST" URL "/import/importerCourses"
    Then Code HTTP externe 200 recu
    And la course avec idImmuable "2148:1187-F-8824-20251110" doit être présente en base
    And json contenu dans le corps de la requete externe "testdata/tnr_sc03_provider_ancien/json/request/SC03_T05_retournerCourseOPE.json"
    When Requete HTTP externe methode "POST" URL "/supervision/retournerCourseOPE"
    Then Code HTTP externe 200 recu
    And la réponse externe contient "type"
    And la réponse externe contient "COURSE_OPE"
    And la réponse externe contient le numeroCourse "8824"

  @TNR_SC03_T06
  Scenario: Réception d'une course commerciale complète avec transco (TNR-SC03-T06)
    Given la course commerciale avec idImmuable "2148:1187-F-170201-2025-10-24" n'existe pas en base
    And json contenu dans le corps de la requete externe "testdata/tnr_sc03_provider_ancien/json/request/SC03_T06_ImporterCourseCommerciale_avecService.json"
    When Requete HTTP externe methode "POST" URL "/import/importerCoursesCommerciales"
    Then Code HTTP externe 200 recu
    And la réponse externe contient "coursesCreees"
    And la liste externe "coursesModifiees" doit être vide
    And la course commerciale avec idImmuable "2148:1187-F-170201-2025-10-24" doit être présente en base