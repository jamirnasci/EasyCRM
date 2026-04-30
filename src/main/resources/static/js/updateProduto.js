  const input = document.getElementById("inputImagem");
  const preview = document.getElementById("preview");

  input.addEventListener("change", function() {
    const file = this.files[0];

    if (file) {
      const reader = new FileReader();

      reader.addEventListener("load", function() {
        preview.src = reader.result;
      });

      reader.readAsDataURL(file);
    }
  });