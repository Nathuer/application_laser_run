Projet de Gabriel Alandette
alandettegabriel@gmail.com

La notation se fera sur la branche master.

Fonctionnalités fonctionnelles:
- récupération des catégories depuis le fichier json
- affichage de la catégorie choisie ainsi que de ces informations
- choix d'une catégorie pour s'entraîner
- le temps total est enregistré et stocké en base de donnée
- le temps de tir et de courses sont enregistrés et stockés en base de donnée
- avertissement sonore si les 50 secondes de tir sont dépassées
- affichage des résultats de l'entraînement à la clotûre de l'application
- calcul de vitesse moyenne
- le nombre de cibles manquées est correct
- on peut consulter ces précedents entraînements
- on a accès à une carte avec des stades dessus pour s'entraîner
- on a le temps moyen d'entraînement, de course, de tir et le nombre moyen de cibles ratées
- présence des tests expressos
- présence de moyenne par rapport aux catégories

Fonctionnalités non fonctionnelles:


Bugs:
- si on relance un entraînement sans fermé l'application, toutes les informations du précedent entraînement s'ajoutent au nouveau
- tout les tests expressos ne fonctionnent pas
- les valeurs données sur la page de stats à la fin de l'entraînement ne sont pas toujours juste


J'ai utilisé ROOM pour stocker mes données car c'est une fonctionnalité que j'avais déjà utiliser au cours de mon BTS.
Pour faire les tests expressos, j'ai essayé d'utiliser Matches afin de simuler une action de click mais ça n'a pas fonctionné correctement

