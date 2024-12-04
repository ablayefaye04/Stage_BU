function initCharts(stats, annees, licenceCounts, masterCounts, doctoratCounts) {
    // Graphique en barre des mémoires par type
     console.log("Données injectées pour les graphiques :", {
            stats,
            annees,
            licenceCounts,
            masterCounts,
            doctoratCounts,
        });

  //diagrame en barre avec courbe
  const statsChartCtx = document.getElementById('statsChart').getContext('2d');
  new Chart(statsChartCtx, {
      type: 'bar',
      data: {
          labels: ['Licence', 'Master', 'Doctorat'],
          datasets: [
              {
                  label: 'Nombre de Mémoires',
                  data: stats,  // Les données des mémoires
                  backgroundColor: 'rgba(27, 154, 118, 0.6)', // Vert clair translucide
                  borderColor: '#1b9a76', // Contour des barres
                  borderWidth: 1,
                  barThickness: 30 // Épaisseur des barres
              },
              {
                  label: 'Tendance',
                  data: stats, // Les mêmes données, utilisées pour la courbe
                  type: 'line',
                  borderColor: '#004d40', // Une couleur sombre pour la courbe
                  tension: 0.4, // Lissage de la courbe
                  pointBackgroundColor: '#004d40', // Couleur des points sur la courbe
                  pointRadius: 4, // Taille des points
                  fill: false // Désactive le remplissage
              }
          ]
      },
      options: {
          responsive: true,
          maintainAspectRatio: true,  // Conserver un rapport d'aspect constant
          plugins: {
              legend: {
                  position: 'top',
              },
              tooltip: {
                  callbacks: {
                      label: function(tooltipItem) {
                          return tooltipItem.raw + ' mémoires';
                      }
                  }
              }
          },
          scales: {
              x: {
                  beginAtZero: true
              },
              y: {
                  beginAtZero: true
              }
          }
      }
  });

//diagrammes circulaires des types de memoires
const pieChartCtx = document.getElementById('pieChart').getContext('2d');
new Chart(pieChartCtx, {
    type: 'pie',
    data: {
        labels: ['Licence', 'Master', 'Doctorat'],
        datasets: [
            {
                data: stats,  // Les données des mémoires
                backgroundColor: [
                    '#98FF98', // Vert clair
                    '#1b9a76', // Vert moyen
                    '#004d40'  // Vert sombre
                ],
                borderColor: '#ffffff', // Bordure blanche pour une meilleure séparation
                borderWidth: 2
            }
        ]
    },
    options: {
        responsive: true,
        maintainAspectRatio: true,  // Garder un rapport d'aspect constant
        plugins: {
            legend: {
                position: 'top',
            },
            tooltip: {
                callbacks: {
                    label: function(tooltipItem) {
                        const label = tooltipItem.label;
                        const value = tooltipItem.raw;
                        return `${label}: ${value} mémoires`;
                    }
                }
            }
        }
    }
});

    // Graphique linéaire de l'évolution des mémoires par année
    const evolutionChartCtx = document.getElementById('evolutionCharts').getContext('2d');
    new Chart(evolutionChartCtx, {
        type: 'line',
        data: {
            labels: annees,  // Utiliser les années
            datasets: [
                {
                    label: 'Licence',
                    data: licenceCounts,  // Nombre de mémoires de Licence par année
                     borderColor: '#98FF98', // Vert clair pour la courbe
                     fill: false,
                    tension: 0.4 // Courbe plus lissée
                },
                {
                    label: 'Master',
                    data: masterCounts,  // Nombre de mémoires de Master par année
                    borderColor: '#1b9a76', // Une nuance de vert un peu plus foncée pour Master
                    fill: false,
                    tension: 0.4
                },
                {
                    label: 'Doctorat',
                    data: doctoratCounts,  // Nombre de mémoires de Doctorat par année
                    borderColor: '#004d40', // Une couleur encore plus foncée pour Doctorat
                    fill: false,
                    tension: 0.4
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: true,
            plugins: {
                legend: {
                    position: 'top',
                },
                tooltip: {
                    callbacks: {
                        label: function(tooltipItem) {
                            return tooltipItem.raw + ' mémoires';
                        }
                    }
                }
            },
            scales: {
                x: {
                    beginAtZero: true
                }
            }
        }
    });
}

