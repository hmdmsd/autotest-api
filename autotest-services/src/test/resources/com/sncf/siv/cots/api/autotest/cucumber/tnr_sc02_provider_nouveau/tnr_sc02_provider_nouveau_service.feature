@TNR_SC02
Feature: Provider nouveau service - Tests de non-régression (TNR)
  En tant que système COTS
  Je veux vérifier le bon fonctionnement du provider nouveau service

  Background:
    Given les données de test TNR_SC02 sont nettoyées

  @TNR_SCN02_T01
  Scenario: Créer une ECP PTA simple (TNR-SCN02-T01)
    Given l'ECP avec idImmuable "2148:1187-G-88865-20251130" n'existe pas en base
    And json contenu dans le corps de la requete externe "testdata/tnr_sc02_provider_nouveau/json/request/CU29_SCN01_T01_importerECPSimple_PTA.json"
    When Requete HTTP externe methode "POST" URL "/import/importerEtatsCoursesProductibles"
    Then Code HTTP externe 200 recu
    And la réponse externe contient "etatsCoursesProductiblesCrees"
    And la réponse externe contient le numeroCourse "88865"
    And l'ECP avec idImmuable "2148:1187-G-88865-20251130" doit être présent en base
    And l'ECP avec idImmuable "2148:1187-G-88865-20251130" doit avoir le numeroCourse "88865"

  @TNR_SC02_T02
  Scenario: Créer un ECP en OPE non couplé à partir d'un ECP PTA (TNR-SC02-T02)
    Given l'ECP avec idImmuable "2148:1187-F-8363-20251023" n'existe pas en base
    And json contenu dans le corps de la requete externe "testdata/tnr_sc02_provider_nouveau/json/request/SC02_T02_ECP_PTA_EN_BDD.json"
    When Requete HTTP externe methode "POST" URL "/import/importerEtatsCoursesProductibles"
    Then Code HTTP externe 200 recu
    And l'ECP avec idImmuable "2148:1187-F-8363-20251023" doit être présent en base
    Given json pour retourner ECP OPE avec idImmuable "2148:1187-F-8363-20251023"
    When Requete HTTP externe methode "POST" URL "/supervision/retournerEtatCourseProductibleOPE"
    Then Code HTTP externe 200 recu
    And la réponse externe correspond au modèle attendu "testdata/tnr_sc02_provider_nouveau/json/response/SC02_T02_Reponse_du_service.json" en excluant les champs dynamiques

  @TNR_SC02_T03
  Scenario: Importer une etatCourseDistribuable cas avecService (TNR-SCN02_T03)
    Given l'ECD avec idImmuable "2148:1187-F-230105-2025-11-28" n'existe pas en base
    And json contenu dans le corps de la requete externe "testdata/tnr_sc02_provider_nouveau/json/request/SCN01_T03_Flux_ImporterEtatCourseCommerciale_avecService.json"
    When Requete HTTP externe methode "POST" URL "/import/importerEtatsCoursesDistribuables"
    Then Code HTTP externe 200 recu
    And la réponse externe contient les listes "etatsCoursesDistribuablesCrees" et "etatsCoursesDistribuablesModifies"
    And la liste externe "etatsCoursesDistribuablesModifies" doit être vide
    And la liste externe "etatsCoursesDistribuablesCrees" doit contenir l'identifiant externe de l'ecd importée
    And la réponse externe correspond au modèle attendu "testdata/tnr_sc02_provider_nouveau/json/response/SCN01_T03_Reponse_Swagger.json"
    And l'ECD avec idImmuable "2148:1187-F-230105-2025-11-28" doit être présent en base
    And l'ECD avec idImmuable "2148:1187-F-230105-2025-11-28" doit avoir le numeroCourse "230105"

  @TNR_SC02_T04
  Scenario: Créer un dossier événement PTP sans courses impactés (TNR-SC02-T04)
    Given le dossier événement avec identifiantDansSystemeCreateur "CU25SCN01T01" n'existe pas en base
    And json contenu dans le corps de la requete externe "testdata/tnr_sc02_provider_nouveau/json/request/SC02_T04_Flux_DEVT_à_importer.json"
    When Requete HTTP externe methode "POST" URL "/import/importerDossierEvenement"
    Then Code HTTP externe 200 recu
    And la réponse externe correspond au modèle attendu "testdata/tnr_sc02_provider_nouveau/json/response/SC02_T04_Réponse_du_service_attendue.json" en excluant le champ id généré
    And le dossier événement avec identifiantDansSystemeCreateur "CU25SCN01T01" doit être présent en base

  @TNR_SC02_T05
  Scenario: Créer un etatCourseObservee tous les champs sont renseignés (TNR-SC02-T05)
    Given l'etatCourseObservee avec idImmuable "2148:8080-F-1187-2025-05-05" n'existe pas en base
    And json contenu dans le corps de la requete externe "testdata/tnr_sc02_provider_nouveau/json/request/SCN02_T05_Créer_un_etatCourseObservee_tous_les_champs_sont_renseignés.json"
    When Requete HTTP externe methode "POST" URL "/import/importerEtatCourseObservee"
    Then Code HTTP externe 200 recu
    And la réponse externe contient l'etatCourseObservee avec les champs générés automatiquement
    And l'etatCourseObservee avec idImmuable "2148:8080-F-1187-2025-05-05" doit être présent en base