document.addEventListener("DOMContentLoaded", function(){
    
    const addBookForm = document.getElementById("addBookForm");
    const parrafoAlerta = document.createElement("P");
    const tituloElement = document.getElementById("nombre");
    const autorElement = document.getElementById("autor");
    const cantpagElement = document.getElementById("cantidadPaginas");
    const copiasElement = document.getElementById("copias");
    const sinopsisElement = document.getElementById("sinopsis");
    const precioElement = document.getElementById("precio");
    
    const imageElement = document.getElementById("imagen");
    const imagenPreview = document.getElementById("imagenPreview");
    const imagenContainer = document.getElementById("imagenContainer");
    
    addBookForm.addEventListener("submit", function(e){
        e.preventDefault();
        
        const datos = new FormData();
        datos.append("action","add");
        datos.append("nombre", tituloElement.value);
        datos.append("autor", autorElement.value);
        datos.append("cantidadPaginas", cantpagElement.value);
        datos.append("precio", copiasElement.value);
        datos.append("imagen", imageElement.files[0]);
        datos.append("copias", copiasElement.value);
        datos.append("sinopsis", sinopsisElement.value);
        
        fetch(`/app/books`, {
            method: "POST",
            body: datos
        })
                .then(response => response.json())
                .then(data =>{
                parrafoAlerta.textContent = data.message;
                addBookForm.appendChild(parrafoAlerta);
                
                setTimeout(()=>{
                    parrafoAlerta.remove();
                    tituloElement.value = "";
                    autorElement.value = "";
                    cantpagElement.value = "";
                    precioElement.value = "";
                    imageElement.value = "";
                    copiasElement.value = "";
                    sinopsisElement.value = "";
                    imagenContainer.classList.add("d-none");
                },3000);
        });
    });
    
    imageElement.addEventListener("change", function(){
        const selectedImage = imageElement.files[0];
        
        if(selectedImage){
            const reader = new FileReader();
            reader.onload = function(e){
                imagenPreview.src = e.target.result;
                imagenContainer.classList.remove("d-none");
            };
            reader.readAsDataURL(selectedImage);
        }else{
            imagenPreview.src = "";
        }
    });
});

