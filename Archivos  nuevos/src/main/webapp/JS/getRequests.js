document.addEventListener("DOMContentLoaded", function(){
    const booksCards = document.getElementById("booksCards");
    const books = [];

    function loadBookList() {
        fetch("/app/books?action=getAll")
                .then(response => response.json())
                .then(data => {
                    data.forEach(book => {
                        books.push(book);
                        booksCards.innerHTML += `
                            <div class="col-md-3 mb-4 ident" data-book-id="${book.idlibros}">
                                <div class="card h-100 animate-hover-card">
                                    <img src="data:image/jpeg;base64,${book.imagenBase64}" class="card-img-top h-75" alt="Imagen Portada de Libro">
                                    <div class="card-body">
                                        <h5 class="card-title">${book.nombre}</h5>
                                        <p class="card-text">${book.sinopsis}</p>
                                    </div>
                                </div>
                            </div>
                        `;
                    });
                });
    }
    function filterBooks(palabra) {
        const librosFiltrados = books.filter(book =>{
            return book.nombre.toLowerCase().includes(palabra.toLowerCase());
        });
        booksCards.innerHTML = "";

        librosFiltrados.forEach(book =>{
            const card = document.createElement("div");
            card.className = "col-md-3 mb-4 ident";
            card.setAttribute("data-book-id", book.idlibros);
            card.innerHTML = `
                <div class="card h-100 animate-hover-card">
                    <img src="data:image/jpeg;base64,${book.imagenBase64}" class="card-img-top h-75" alt="Imagen Portada de Libro">
                    <div class="card-body">
                      <h5 class="card-title">${book.nombre}</h5>
                       <p class="card-text">${book.sinopsis}</p>
                    </div>
                </div>
            `;
            booksCards.appendChild(card);
        });
    }
    
    const searchForm = document.querySelector("form[role='search']");
    searchForm.addEventListener("submit", function(e){
        e.preventDefault();
        const searchTerm = searchForm.querySelector("input[type='search']").value;
        filterBooks(searchTerm);
    });
    
    booksCards.addEventListener("click", function(e){
        const clickedCard = e.target.closest(".ident");
        if(!clickedCard){
            return;
        }
        const bookId = clickedCard.dataset.bookId;
        
        fetch(`/app/books?action=getDetails&id=${bookId}`)
                .then(response => response.json())
                .then(bookDetails =>{
                    const queryParams = new URLSearchParams({
                        id : bookDetails.idlibros
                    });
                    
                    window.location.href = `app/Pages/bookDetails.html?${queryParams.toString()}`;
        })
        .catch(error => console.error("Error en la solicitud GET:", error));
    });
    
    loadBookList();

});



