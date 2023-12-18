
document.addEventListener("DOMContentLoaded", function(){
    const moviesCards = document.getElementById("moviesCards");
    const movies =[];
    
    function loadMoviesList(){
        fetch("/app/movies?action=getAll")
                .then(response=> response.json())
                .then(data =>{
                    data.forEach(movie =>{
                        movies.push(movie);
                        moviesCards.innerHTML += `
                            <div class="col-md-3 mb-4 ident" data-movie-id="${movie.idmovies}">
                                <div class="card h-100 animate-hover-card">
                                    <img src="data:image/jpeg;base64,${movie.imagenBase64}" class="card-img-top h-75" alt="Portada de una Pelicula">
                                    <div class="card-body">
                                        <h5 class="card-tittle">${movie.nombre}</h5>
                                        <p class="card-text">${movie.sinopsis}</p>
                                    </div>
                                </div>
                            </div>
                        `;
                    });
                });
    }
    
    function filterMovies(palabra){
        const moviesFilters = movies.filter( movie=>{
            return movie.nombre.toLowerCase().includes(palabra.toLowerCase());
        });
        
        moviesCards.innerHTML = "";
        
         moviesFilters.forEach(movie => {
            const card = document.createElement("div");
            card.className = "col-md-3 mb-4 ident";
            card.setAttribute("data-movie-id", movie.idmovies);
            card.innerHTML = `
            <div class="card h-100 animate-hover-card">
                <img src="data:image/jpeg;base64,${movie.imagenBase64}" class="card-img-top h-75" alt="imagen de portada">
                <div class="card-body">
                    <h5 class="card-title">${movie.nombre}</h5>
                    <p class="card-text">${movie.sinopsis}</p>
                </div>
            </div>
        `;
            moviesCards.appendChild(card);
        });
    }

    const searchForm = document.querySelector("form[role='search']");
    searchForm.addEventListener("submit", function(e){
       e.preventDefault();
       const searchTerm = searchForm.querySelector("input[type='search']").value;
       filterMovies(searchTerm);
    });

    moviesCards.addEventListener("click", function(e){
        const clickedCard = e.target.closest(".ident");
        if(!clickedCard){
            return;
        }
        
        const movieId = clickedCard.dataset.movieId;
        
        fetch(`/app/movies?action=getDetails&id=${movieId}`)
                .then(response => response.json())
                .then(movieDetails =>{
                    const queryParams =  new URLSearchParams({
                       id : movieDetails.idmovies
                    });
                    
                    window.location.href = `/app/pages/movieDetails.html?${queryParams.toString()}`;
                })
                .catch(error => console.error("Error en la solicitud GET:", error));
    });
    
    loadMoviesList();
    
    
});

