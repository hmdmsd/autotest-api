@TNR_SC05
Feature: Consumer ancien service - Tests de non-régression (TNR)
  En tant que système COTS
  Je veux vérifier le bon fonctionnement du consumer ancien service

  Background:
    Given les données de test TNR_SC05 sont nettoyées

  @TNR_SC05_T01
  Scenario: Obtenir une course PTA, PTP et OPE (TNR-SC05-T01)
    # Prérequis: DEVT à importer
    Given le dossier événement avec identifiantDansSystemeCreateur "5T-2025-12-02" n'existe pas en base
    And json contenu dans le corps de la requete externe "testdata/tnr_sc05_consumer_ancien/json/request/SC05_T01_Prerequis_DEVT_a_Inserer.json"
    When Requete HTTP externe methode "POST" URL "/import/importerDossierEvenement"
    Then Code HTTP externe 200 recu
    And le dossier événement avec identifiantDansSystemeCreateur "5T-2025-12-02" doit être présent en base
    # Prérequis: ECP PTA à importer
    Given l'ECP avec idImmuable "2148:1187-F-WN986-20250301" n'existe pas en base
    And json contenu dans le corps de la requete externe "testdata/tnr_sc05_consumer_ancien/json/request/SC05_T01_Prerequis_coursePTA_a_Inserer.json"
    When Requete HTTP externe methode "POST" URL "/import/importerEtatsCoursesProductibles"
    Then Code HTTP externe 200 recu
    And l'ECP avec idImmuable "2148:1187-F-WN986-20250301" doit être présent en base
    # Prérequis: ECP PTP à importer (cours 88865)
    Given l'ECP avec idImmuable "2148:1187-F-88865-20251130" n'existe pas en base
    And json contenu dans le corps de la requete externe "testdata/tnr_sc05_consumer_ancien/json/request/SC05_T01_Prerequis_coursePTP_a_Inserer.json"
    When Requete HTTP externe methode "POST" URL "/import/importerEtatsCoursesProductibles"
    Then Code HTTP externe 200 recu
    And l'ECP avec idImmuable "2148:1187-F-88865-20251130" doit être présent en base
    # Prérequis: Course OPE à importer
    Given l'ECP avec idImmuable "2849:20250318-0001" n'existe pas en base
    And json contenu dans le corps de la requete externe "testdata/tnr_sc05_consumer_ancien/json/request/SC05_T01_Prerequis_courseOPE_a_Inserer.json"
    When Requete HTTP externe methode "POST" URL "/import/importerEtatsCoursesProductibles"
    Then Code HTTP externe 200 recu
    And l'ECP avec idImmuable "2849:20250318-0001" doit être présent en base
    # Test cas PTA
    When Requete HTTP consumer GET URL "/courses" avec parametres numeroCourse "WN986" codeService "2025" dateDebutPeriode "2025-12-02" dateFinPeriode "2025-12-02"
    Then Code HTTP consumer 200 recu
    And la réponse consumer correspond au modèle attendu "testdata/tnr_sc05_consumer_ancien/json/response/SC05_T01_Reponse_GET_coursePTA.json"
    # Test cas PTP
    When Requete HTTP consumer GET URL "/courses" avec parametres numeroCourse "88865" codeService "A2025" typeCourse "PTP"
    Then Code HTTP consumer 200 recu
    And la réponse consumer correspond au modèle attendu "testdata/tnr_sc05_consumer_ancien/json/response/SC05_T01_Reponse_GET_coursePTP.json"
    # Test cas OPE
    When Requete HTTP consumer GET URL "/courses" avec parametres numeroCourse "11999" codeService "2025" typeCourse "OPE"
    Then Code HTTP consumer 200 recu
    And la réponse consumer correspond au modèle attendu "testdata/tnr_sc05_consumer_ancien/json/response/SC05_T01_Reponse_GET_courseOPE.json"

  @TNR_SC05_T02
  Scenario: Récupérer une courseCommerciale cas PTA (TNR-SC05-T02)
    # Prérequis: Course commerciale à importer
    Given la course commerciale avec idImmuable "2148:1187-F-081702-20251223" n'existe pas en base
    And json contenu dans le corps de la requete externe "testdata/tnr_sc05_consumer_ancien/json/request/SC05_T02_Prerequis_CourseCommerciale_a_Inserer.json"
    When Requete HTTP externe methode "POST" URL "/import/importerCoursesCommerciales"
    Then Code HTTP externe 200 recu
    And la course commerciale avec idImmuable "2148:1187-F-081702-20251223" doit être présente en base
    # Test GET /coursesCommerciales
    When Requete HTTP consumer GET URL "/coursesCommerciales" avec parametres numeroCourse "081702" indicateurFer "FERRE" codeCompagnieTransporteur "1187" dateDebutPeriode "2025-12-23" dateFinPeriode "2025-12-23"
    Then Code HTTP consumer 200 recu
    And la réponse consumer correspond au modèle attendu "testdata/tnr_sc05_consumer_ancien/json/response/SC05_T02_Reponse_GET_coursesCommerciales.json"

  @TNR_SC05_T03
  Scenario: Recherche un etatCourseProductible PTA (TNR-SC05-T03)
    # Prérequis: ECP PTA à importer
    Given l'ECP avec idImmuable "2148:1187-F-88865-20251130" n'existe pas en base
    And json contenu dans le corps de la requete externe "testdata/tnr_sc05_consumer_ancien/json/request/SC05_T03_Prerequis_ECP_a_Inserer.json"
    When Requete HTTP externe methode "POST" URL "/import/importerEtatsCoursesProductibles"
    Then Code HTTP externe 200 recu
    And l'ECP avec idImmuable "2148:1187-F-88865-20251130" doit être présent en base
    # Test GET /etatsCoursesProductibles
    When Requete HTTP consumer GET URL "/etatsCoursesProductibles" avec parametre idImmuable "2148:1187-F-88865-20251130"
    Then Code HTTP consumer 200 recu
    And un seul objet est retourné dans la réponse consumer
    And la réponse consumer contient le numeroCourse "88865"
    And la réponse consumer correspond au modèle attendu "testdata/tnr_sc05_consumer_ancien/json/response/SC05_T03_Reponse_GET_etatsCoursesProductibles.json"

  @TNR_SC05_T04
  Scenario: Recherche de course OPE - Clé sillon présente (TNR-SC05-T04)
    # Prérequis: ECP1 à importer
    Given l'ECP avec idImmuable "2148:1187-F-11224-20251130" n'existe pas en base
    And json contenu dans le corps de la requete externe "testdata/tnr_sc05_consumer_ancien/json/request/SC05_T04_Prerequis_ECP1_a_Inserer.json"
    When Requete HTTP externe methode "POST" URL "/import/importerEtatsCoursesProductibles"
    Then Code HTTP externe 200 recu
    And l'ECP avec idImmuable "2148:1187-F-11224-20251130" doit être présent en base
    # Prérequis: ECP2 à importer
    Given l'ECP avec idImmuable "2147:1187-F-11224-20251130" n'existe pas en base
    And json contenu dans le corps de la requete externe "testdata/tnr_sc05_consumer_ancien/json/request/SC05_T04_Prerequis_ECP2_a_Inserer.json"
    When Requete HTTP externe methode "POST" URL "/import/importerEtatsCoursesProductibles"
    Then Code HTTP externe 200 recu
    And l'ECP avec idImmuable "2147:1187-F-11224-20251130" doit être présent en base
    # Prérequis: ECP3 à importer
    Given l'ECP avec idImmuable "2148:1187-F-11224-20251129" n'existe pas en base
    And json contenu dans le corps de la requete externe "testdata/tnr_sc05_consumer_ancien/json/request/SC05_T04_Prerequis_ECP3_a_Inserer.json"
    When Requete HTTP externe methode "POST" URL "/import/importerEtatsCoursesProductibles"
    Then Code HTTP externe 200 recu
    And l'ECP avec idImmuable "2148:1187-F-11224-20251129" doit être présent en base
    # Tests RetournerClesCoursesParSillon - 7 cas de test
    When Requete HTTP consumer POST URL "/courses/retournerClesCoursesParSillon" avec json "testdata/tnr_sc05_consumer_ancien/json/request/SC05_T04_Request_RetournerClesCoursesParSillon_1.json"
    Then Code HTTP consumer 200 recu
    And la réponse consumer correspond au modèle attendu "testdata/tnr_sc05_consumer_ancien/json/response/SC05_T04_Reponse_RetournerClesCoursesParSillon_1.json"
    When Requete HTTP consumer POST URL "/courses/retournerClesCoursesParSillon" avec json "testdata/tnr_sc05_consumer_ancien/json/request/SC05_T04_Request_RetournerClesCoursesParSillon_2.json"
    Then Code HTTP consumer 200 recu
    And la réponse consumer correspond au modèle attendu "testdata/tnr_sc05_consumer_ancien/json/response/SC05_T04_Reponse_RetournerClesCoursesParSillon_2.json"
    When Requete HTTP consumer POST URL "/courses/retournerClesCoursesParSillon" avec json "testdata/tnr_sc05_consumer_ancien/json/request/SC05_T04_Request_RetournerClesCoursesParSillon_3.json"
    Then Code HTTP consumer 200 recu
    And la réponse consumer correspond au modèle attendu "testdata/tnr_sc05_consumer_ancien/json/response/SC05_T04_Reponse_RetournerClesCoursesParSillon_3.json"
    When Requete HTTP consumer POST URL "/courses/retournerClesCoursesParSillon" avec json "testdata/tnr_sc05_consumer_ancien/json/request/SC05_T04_Request_RetournerClesCoursesParSillon_4.json"
    Then Code HTTP consumer 200 recu
    And la réponse consumer correspond au modèle attendu "testdata/tnr_sc05_consumer_ancien/json/response/SC05_T04_Reponse_RetournerClesCoursesParSillon_4.json"
    When Requete HTTP consumer POST URL "/courses/retournerClesCoursesParSillon" avec json "testdata/tnr_sc05_consumer_ancien/json/request/SC05_T04_Request_RetournerClesCoursesParSillon_5.json"
    Then Code HTTP consumer 200 recu
    And la réponse consumer correspond au modèle attendu "testdata/tnr_sc05_consumer_ancien/json/response/SC05_T04_Reponse_RetournerClesCoursesParSillon_5.json"
    When Requete HTTP consumer POST URL "/courses/retournerClesCoursesParSillon" avec json "testdata/tnr_sc05_consumer_ancien/json/request/SC05_T04_Request_RetournerClesCoursesParSillon_6.json"
    Then Code HTTP consumer 200 recu
    And la réponse consumer correspond au modèle attendu "testdata/tnr_sc05_consumer_ancien/json/response/SC05_T04_Reponse_RetournerClesCoursesParSillon_6.json"
    When Requete HTTP consumer POST URL "/courses/retournerClesCoursesParSillon" avec json "testdata/tnr_sc05_consumer_ancien/json/request/SC05_T04_Request_RetournerClesCoursesParSillon_7.json"
    Then Code HTTP consumer 200 recu
    And la réponse consumer correspond au modèle attendu "testdata/tnr_sc05_consumer_ancien/json/response/SC05_T04_Reponse_RetournerClesCoursesParSillon_7.json"

  @TNR_SC05_T05
  Scenario: Demande de journées PTP valide (TNR-SC05-T05)
    Given le dossier événement avec identifiantDansSystemeCreateur "5J-2025-12-14" n'existe pas en base
    And le dossier événement avec identifiantDansSystemeCreateur "5J-2025-12-15" n'existe pas en base
    And le dossier événement avec identifiantDansSystemeCreateur "5H-2025-12-16" n'existe pas en base
    And le dossier événement avec identifiantDansSystemeCreateur "5H-2025-12-17" n'existe pas en base
    And le dossier événement avec identifiantDansSystemeCreateur "5J-2025-12-16" n'existe pas en base
    And json contenu dans le corps de la requete externe "testdata/tnr_sc05_consumer_ancien/json/request/SC05_T05_Prerequis_DEVT1_a_Inserer.json"
    When Requete HTTP externe methode "POST" URL "/import/importerDossierEvenement"
    Then Code HTTP externe 200 recu
    And json contenu dans le corps de la requete externe "testdata/tnr_sc05_consumer_ancien/json/request/SC05_T05_Prerequis_DEVT2_a_Inserer.json"
    When Requete HTTP externe methode "POST" URL "/import/importerDossierEvenement"
    Then Code HTTP externe 200 recu
    And json contenu dans le corps de la requete externe "testdata/tnr_sc05_consumer_ancien/json/request/SC05_T05_Prerequis_DEVT3_a_Inserer.json"
    When Requete HTTP externe methode "POST" URL "/import/importerDossierEvenement"
    Then Code HTTP externe 200 recu
    And json contenu dans le corps de la requete externe "testdata/tnr_sc05_consumer_ancien/json/request/SC05_T05_Prerequis_DEVT4_a_Inserer.json"
    When Requete HTTP externe methode "POST" URL "/import/importerDossierEvenement"
    Then Code HTTP externe 200 recu
    And json contenu dans le corps de la requete externe "testdata/tnr_sc05_consumer_ancien/json/request/SC05_T05_Prerequis_DEVT5_a_Inserer.json"
    When Requete HTTP externe methode "POST" URL "/import/importerDossierEvenement"
    Then Code HTTP externe 200 recu
    And le dossier événement avec identifiantDansSystemeCreateur "5J-2025-12-14" doit être présent en base
    And le dossier événement avec identifiantDansSystemeCreateur "5J-2025-12-15" doit être présent en base
    And le dossier événement avec identifiantDansSystemeCreateur "5H-2025-12-16" doit être présent en base
    And le dossier événement avec identifiantDansSystemeCreateur "5H-2025-12-17" doit être présent en base
    And le dossier événement avec identifiantDansSystemeCreateur "5J-2025-12-16" doit être présent en base
    # STEP 1: Appeler le service - 1 seul CTR inclus
    When Requete HTTP consumer GET URL "/dossierEvenements" avec parametres dateDebutPeriode "2025-01-15" dateFinPeriode "2025-01-16"
    Then Code HTTP consumer 200 recu
    And la réponse consumer correspond au modèle attendu "testdata/tnr_sc05_consumer_ancien/json/response/SC05_T05_Reponse_Step1_Get_DossierEvenement.json"
    And la réponse consumer contient le champ "validePourDiffusion" avec la valeur "true"
    # STEP 2: Appeler le service - Plusieurs CTR inclus
    When Requete HTTP consumer GET URL "/dossierEvenements" avec parametres dateDebutPeriode "2025-01-15" dateFinPeriode "2025-01-16" codeTransporteurResponsable "5G"
    Then Code HTTP consumer 200 recu
    And la réponse consumer correspond au modèle attendu "testdata/tnr_sc05_consumer_ancien/json/response/SC05_T05_Reponse_Step2_Get_DossierEvenement.json"
    And la réponse consumer contient le champ "validePourDiffusion" avec la valeur "true"