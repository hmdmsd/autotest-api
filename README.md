# Autotest API 

## Description

Autotest est un Spring Boot de test automatique conçu pour valider le bon fonctionnement des services Spring Boot COTS en environnement d'homologation.

## Fonctionnalités

### Endpoints Disponibles

| Endpoint | Méthode | Description |
|----------|---------|-------------|
| `/api/health/check` | GET | Vérification état de santé (204) |
| `/api/test/execute` | POST | Exécution de tests automatisés |