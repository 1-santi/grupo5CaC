document.addEventListener("DOMContentLoaded", function(){
    const booksCards = document.getElementById("booksCards");
    const books = [];
    
    function loadBookList(){
        fetch("/app/books?action=getAll")
                .then(response => response.json())
                .then(data =>{
                    data.forEach(book =>{
                        books.push(book);
                        booksCards.innerHTML += `
                            <div class="col-md-3 mb-4 ident" data-book-id="${book.idlibros}">
                                <div class="card h-100 animate-hover-card">
                                    <img src="data:image/jpeg;base64,${book.imagenBase64}" class="card-img-top h-75" alt="Imagen Portada de Libro">
                                    <div class="card-body">
                                        <h5 class="card-tittle">${book.nombre}</h5>
                                        <p class="card-text">${book.sinopsis}</p>
                                    </div>
                                </div>
                            </div>
                        `
                    });
        });
    }
    loadBookList();
});



