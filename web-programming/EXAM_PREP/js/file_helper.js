const fileInput = document.querySelector('#upload');
const previewImg = document.querySelector('#preview');

fileInput.addEventListener('change', (e) => {
    const file = e.target.files[0];
    if (!file) return;

    // FileReader is the key API here
    const reader = new FileReader();
    
    // When the file is read, run this:
    reader.onload = (event) => {
        // event.target.result contains the base64 data
        previewImg.src = event.target.result; 
    };

    // Start reading
    reader.readAsDataURL(file);
});
