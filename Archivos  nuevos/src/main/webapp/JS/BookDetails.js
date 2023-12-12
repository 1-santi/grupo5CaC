document.addEventListener("DOMContentLoaded", function () {
    const queryParams = new URLSearchParams(window.location.search);
    const bookDetailId = {
        id: queryParams.get("id")
    };
    const bookDetailsContainer = document.getElementById("bookDetails");
    const btnEliminarElement = document.getElementById("btnEliminar");
    const btnModificarElement = document.getElementById("btnModificar");
    const btnGuardarElement = document.getElementById("btnGuardar");
    const btnContainerElement = document.getElementById("btnContainer");

    let ObjetoLibro = {
        id: 0,
        nombre: "",
        autor: "",
        cantidadPaginas: 0,
        precio: 0,
        imagen: "",
        copias: 0,
        sinopsis: ""
    };
    function loadBook() {

        fetch(`/app/books?action=getById&id=${bookDetailId.id}`)
                .then(response => response.json())
                .then(data => {
                    bookDetailsContainer.innerHTML += `
                <div class="col-md-6" text-center>
                    <div class="clearfix">
                        <img src="data:image/jpeg;base64,${data.imagenBase64}" class="my-4" style="width:75%" alt="Imagen Portada de Libro">
                    </div>
                </div>
                <div class="card-body col-md-6">
                    <ul class="list-group list-group-flush">
                    <li class="list-group-item">
                        <h2 class="card-title">${data.nombre}</h2>
                    </li>
                    <li class="list-group-item">Autor: ${data.autor}</li>
                    <li class="list-group-item">Paginas: ${data.cantidadPaginas}</li>
                    <li class="list-group-item">Sinopsis: ${data.sinopsis}</li>
                    <li class="list-group-item">Copias: ${data.copias}</li>
                    <li class="list-group-item">Autor: ${data.autor}</li>
                    <li class="list-group-item">
                        <h5>Precio: ${data.autor}</h5>
                    </li>
                    </ul>
                </div>
            `;
            
            ObjetoLibro.id =data.id;
            ObjetoLibro.nombre =data.nombre;
            ObjetoLibro.autor =data.autor;
            ObjetoLibro.cantidadPaginas =data.cantidadPaginas;
            ObjetoLibro.precio =data.precio;
            ObjetoLibro.imagen =data.imagen;
            ObjetoLibro.copias =data.copias;
            ObjetoLibro.sinopsis =data.sinopsis;
            
                });
    }
    loadBook();
});


