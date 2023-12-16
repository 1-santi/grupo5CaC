document.addEventListener("DOMContentLoaded", function(){
    
    const addMovieForm = document.getElementById("addMovieForm");
    const parrafoAlerta = document.createElement("P");
    const tituloElement = document.getElementById("nombre");
    const autorElement = document.getElementById("autor");
    const duracionElement = document.getElementById("duracion");
    const copiasElement = document.getElementById("copias");
    const sinopsisElement = document.getElementById("sinopsis");
    const precioElement = document.getElementById("precio");
    
    const imageElement = document.getElementById("imagen");
    const imagenPreview = document.getElementById("imagenPreview");
    const imagenContainer = document.getElementById("imagenContainer");
    
    addMovieFormForm.addEventListener("submit", function(e){
        e.preventDefault();
        
        const datos = new FormData();
        
        datos.append("action","add");
        datos.append("nombre",tituloElement.value);
        datos.append("autor",autorElement.value);
        datos.append("duracion",duracionElement.value);
        datos.append("copias",copiasElement.value);
        datos.append("sinopsis",sinopsisElement.value);
        datos.append("precio",precioElement.value);
        datos.append("imagen",imageElement.files[0]);
        
        fetch("/app/movies", {
            method: "POST",
            body: datos
        })
                .then(response => response.json())
                .then(data=> {
                    parrafoAlerta.textContent = data.message;
                    addMovieForm.appendChild(parrafoAlerta);
                    
                    setTimeout(()=>{
                        parrafoAlerta.remove();
                        tituloElement.value = "";
                        autorElement.value = "";
                        duracionElement.value = "";
                        copiasElement.value = "";
                        sinopsisElement.value = "";
                        precioElement.value = "";
                        imageElement.value = "";
                        imagenContainer.classList.add("d-none");
                    },3000)
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
            imagenPreview.src= "";
        }
    });
    
});

