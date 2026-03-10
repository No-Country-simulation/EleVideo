import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { MainLayout } from "@/components/layout/MainLayout";
import { VideoCard } from "@/components/dashboard/VideoCard";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Upload, Search, Filter, Grid, List } from "lucide-react";
import { getVideos } from "@/services/api";

const Videos = () => {
  const navigate = useNavigate();
  const [videos, setVideos] = useState<any[]>([]);
  const [searchQuery, setSearchQuery] = useState("");
  const [viewMode, setViewMode] = useState<"grid" | "list">("grid");

  useEffect(() => {
    fetchVideos();
  }, []);

  const fetchVideos = async () => {
  try {
    const data = await getVideos(1); // Pasamos ID de proyecto 1 por ejemplo
    setVideos(data); // data ahora es el array de content
  } catch (error) {
    console.error(error);
  }
};

  return (
    <MainLayout
      title="Mis Videos"
      subtitle="Gestiona y organiza todo tu contenido de video"
    >
      <div className="space-y-6">
        <div className="glass rounded-2xl p-4 flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4">
          <div className="flex items-center gap-3 w-full sm:w-auto">
            <div className="relative flex-1 sm:flex-none">
              <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
              <Input
                type="search"
                placeholder="Buscar videos..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="pl-10 w-full sm:w-64 bg-secondary border-0"
              />
            </div>
            
            {/* ✅ BOTÓN CORREGIDO: Usamos un casting (as any) para evitar el error de TS 
                mientras se refresca la caché de las carpetas */}
            <Button 
              {...({ variant: "outline", size: "icon" } as any)}
              aria-label="Filtrar videos"
              title="Filtrar videos"
            >
              <Filter className="w-4 h-4" />
            </Button>
          </div>

          <div className="flex items-center gap-3">
            <div className="flex items-center bg-secondary rounded-lg p-1">
              <button
                onClick={() => setViewMode("grid")}
                aria-label="Vista de cuadrícula"
                title="Vista de cuadrícula"
                className={`p-2 rounded-md ${viewMode === "grid" ? "bg-primary text-primary-foreground" : "text-muted-foreground"}`}
              >
                <Grid className="w-4 h-4" />
              </button>
              <button
                onClick={() => setViewMode("list")}
                aria-label="Vista de lista"
                title="Vista de lista"
                className={`p-2 rounded-md ${viewMode === "list" ? "bg-primary text-primary-foreground" : "text-muted-foreground"}`}
              >
                <List className="w-4 h-4" />
              </button>
            </div>

            <Button
              onClick={() => navigate("/upload")}
              className="gradient-accent shadow-accent-glow"
            >
              <Upload className="w-4 h-4 mr-2" />
              Subir Video
            </Button>
          </div>
        </div>

        <div className={`grid gap-6 ${viewMode === "grid" ? "grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4" : "grid-cols-1"}`}>
          {videos
            .filter(video => video.title?.toLowerCase().includes(searchQuery.toLowerCase()))
            .map((video, index) => (
              <VideoCard
                key={video.id || index}
                title={video.title}
                thumbnail={video.thumbnail || "https://via.placeholder.com/400x225"}
                duration={video.duration || "0:00"}
                views={video.views || "0"}
                likes={video.likes || "0"}
                status={video.status || "draft"}
                platform={video.platform || "YouTube"}
              />
            ))}
        </div>

        {videos.length === 0 && (
          <div className="glass rounded-2xl p-12 text-center">
            <h3 className="text-lg font-semibold">No hay videos aún</h3>
          </div>
        )}
      </div>
    </MainLayout>
  );
};

export default Videos;