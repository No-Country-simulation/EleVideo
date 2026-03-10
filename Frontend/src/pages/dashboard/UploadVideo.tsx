import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { MainLayout } from "@/components/layout/MainLayout";
import { Button } from "@/components/ui/button";

const API_URL = import.meta.env.VITE_API_URL;

const UploadVideo = () => {
  const navigate = useNavigate();
  const [file, setFile] = useState<File | null>(null);
  const [loading, setLoading] = useState(false);

  const handleUpload = async () => {
    if (!file) {
      alert("Selecciona un archivo");
      return;
    }

    const formData = new FormData();
    formData.append("file", file);

    try {
      setLoading(true);

      const response = await fetch(`${API_URL}/api/videos/upload`, {
        method: "POST",
        body: formData,
      });

      if (!response.ok) {
        throw new Error("Error subiendo video");
      }

      const data = await response.json();
      console.log("Video subido:", data);

      alert("Video subido correctamente ✅");

      // Redirigir a la lista
      navigate("/videos");

    } catch (error) {
      console.error(error);
      alert("Error al subir el video ❌");
    } finally {
      setLoading(false);
    }
  };

  return (
    <MainLayout
      title="Subir Video"
      subtitle="Carga tu nuevo contenido"
    >
      <div className="glass rounded-2xl p-8 max-w-xl mx-auto space-y-6">

        <input
          type="file"
          accept="video/*"
          onChange={(e) => setFile(e.target.files?.[0] || null)}
          className="w-full"
        />

        <Button
          onClick={handleUpload}
          disabled={loading}
          className="w-full gradient-accent"
        >
          {loading ? "Subiendo..." : "Subir Video"}
        </Button>

      </div>
    </MainLayout>
  );
};

export default UploadVideo;
