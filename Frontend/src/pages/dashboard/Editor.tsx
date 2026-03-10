import { useState } from "react";
import { MainLayout } from "@/components/layout/MainLayout";
import { Button } from "@/components/ui/button";
import { Slider } from "@/components/ui/slider";
import { 
  Play, 
  Pause, 
  SkipBack, 
  SkipForward, 
  Scissors, 
  Download,
  Smartphone,
  Monitor,
  RotateCcw,
  Sparkles,
  Volume2
} from "lucide-react";

const Editor = () => {
  const [isPlaying, setIsPlaying] = useState(false);
  const [currentTime, setCurrentTime] = useState(30);
  const [volume, setVolume] = useState([75]);
  const [selectedFormat, setSelectedFormat] = useState<"horizontal" | "vertical">("horizontal");

  return (
    <MainLayout 
      title="Crear Short" 
      subtitle="Convierte tus videos horizontales en shorts verticales"
    >
      <div className="grid grid-cols-1 xl:grid-cols-3 gap-6">
        {/* Main Preview Area */}
        <div className="xl:col-span-2 space-y-4">
          {/* Preview */}
          <div className="glass rounded-2xl p-6 space-y-4">
            <div className="flex items-center justify-between mb-4">
              <h3 className="font-semibold text-foreground">Vista Previa</h3>
              <div className="flex items-center gap-2">
                <button
                  onClick={() => setSelectedFormat("horizontal")}
                  className={`flex items-center gap-2 px-3 py-2 rounded-lg transition-colors ${
                    selectedFormat === "horizontal"
                      ? "bg-primary text-primary-foreground"
                      : "bg-secondary text-muted-foreground hover:text-foreground"
                  }`}
                >
                  <Monitor className="w-4 h-4" />
                  <span className="text-sm">16:9</span>
                </button>
                <button
                  onClick={() => setSelectedFormat("vertical")}
                  className={`flex items-center gap-2 px-3 py-2 rounded-lg transition-colors ${
                    selectedFormat === "vertical"
                      ? "bg-accent text-accent-foreground"
                      : "bg-secondary text-muted-foreground hover:text-foreground"
                  }`}
                >
                  <Smartphone className="w-4 h-4" />
                  <span className="text-sm">9:16</span>
                </button>
              </div>
            </div>

            {/* Video Preview */}
            <div className="relative bg-muted rounded-xl overflow-hidden flex items-center justify-center">
              <div className={`relative transition-all duration-300 ${
                selectedFormat === "horizontal" 
                  ? "aspect-video w-full" 
                  : "aspect-[9/16] h-[400px]"
              }`}>
                <img
                  src="https://images.unsplash.com/photo-1559136555-9303baea8ebd?w=800&h=450&fit=crop"
                  alt="Video preview"
                  className="w-full h-full object-cover"
                />
                
                {/* Play/Pause Overlay */}
                <button
                  onClick={() => setIsPlaying(!isPlaying)}
                  className="absolute inset-0 flex items-center justify-center bg-background/20 opacity-0 hover:opacity-100 transition-opacity"
                >
                  <div className="w-16 h-16 rounded-full gradient-primary flex items-center justify-center shadow-glow">
                    {isPlaying ? (
                      <Pause className="w-6 h-6 text-primary-foreground" />
                    ) : (
                      <Play className="w-6 h-6 text-primary-foreground ml-1" />
                    )}
                  </div>
                </button>

                {/* Crop Overlay for Vertical */}
                {selectedFormat === "vertical" && (
                  <div className="absolute inset-0 border-4 border-accent border-dashed rounded-lg pointer-events-none">
                    <div className="absolute -top-6 left-1/2 -translate-x-1/2 px-2 py-1 rounded bg-accent text-accent-foreground text-xs font-medium">
                      Área de recorte
                    </div>
                  </div>
                )}
              </div>
            </div>

            {/* Timeline */}
            <div className="space-y-3">
              <div className="flex items-center gap-4">
                <span className="text-sm text-muted-foreground w-12">
                  {Math.floor(currentTime / 60)}:{String(currentTime % 60).padStart(2, "0")}
                </span>
                <Slider
                  value={[currentTime]}
                  max={180}
                  step={1}
                  onValueChange={(value) => setCurrentTime(value[0])}
                  className="flex-1"
                />
                <span className="text-sm text-muted-foreground w-12">3:00</span>
              </div>

              {/* Controls */}
              <div className="flex items-center justify-center gap-2">
                <Button variant="ghost" size="icon">
                  <SkipBack className="w-5 h-5" />
                </Button>
                <Button
                  onClick={() => setIsPlaying(!isPlaying)}
                  className="w-12 h-12 rounded-full gradient-primary"
                >
                  {isPlaying ? (
                    <Pause className="w-5 h-5" />
                  ) : (
                    <Play className="w-5 h-5 ml-0.5" />
                  )}
                </Button>
                <Button variant="ghost" size="icon">
                  <SkipForward className="w-5 h-5" />
                </Button>
                
                <div className="w-px h-6 bg-border mx-2" />
                
                <Button variant="ghost" size="icon">
                  <Volume2 className="w-5 h-5" />
                </Button>
                <Slider
                  value={volume}
                  max={100}
                  step={1}
                  onValueChange={setVolume}
                  className="w-24"
                />
              </div>
            </div>
          </div>

          {/* Timeline Clips */}
          <div className="glass rounded-2xl p-4">
            <h3 className="font-semibold text-foreground mb-4">Línea de Tiempo</h3>
            <div className="h-20 bg-secondary rounded-lg relative overflow-hidden">
              {/* Waveform visualization mock */}
              <div className="absolute inset-0 flex items-center px-4">
                {Array.from({ length: 50 }).map((_, i) => (
                  <div
                    key={i}
                    className="flex-1 mx-0.5 bg-primary/60 rounded-full"
                    style={{ 
                      height: `${20 + Math.random() * 60}%`,
                    }}
                  />
                ))}
              </div>
              
              {/* Playhead */}
              <div
                className="absolute top-0 bottom-0 w-0.5 bg-accent"
                style={{ left: `${(currentTime / 180) * 100}%` }}
              >
                <div className="absolute -top-1 left-1/2 -translate-x-1/2 w-3 h-3 rounded-full bg-accent" />
              </div>

              {/* Selection */}
              <div
                className="absolute top-0 bottom-0 bg-accent/20 border-x-2 border-accent"
                style={{ left: "10%", width: "30%" }}
              />
            </div>
          </div>
        </div>

        {/* Sidebar Tools */}
        <div className="space-y-4">
          {/* Quick Tools */}
          <div className="glass rounded-2xl p-6 space-y-4">
            <h3 className="font-semibold text-foreground">Herramientas</h3>
            
            <div className="grid grid-cols-2 gap-3">
              <Button variant="outline" className="flex flex-col gap-2 h-auto py-4">
                <Scissors className="w-5 h-5" />
                <span className="text-xs">Cortar</span>
              </Button>
              <Button variant="outline" className="flex flex-col gap-2 h-auto py-4">
                <RotateCcw className="w-5 h-5" />
                <span className="text-xs">Deshacer</span>
              </Button>
              <Button variant="outline" className="flex flex-col gap-2 h-auto py-4 col-span-2 gradient-primary border-0 text-primary-foreground">
                <Sparkles className="w-5 h-5" />
                <span className="text-xs">Auto-detectar mejores momentos</span>
              </Button>
            </div>
          </div>

          {/* Export Options */}
          <div className="glass rounded-2xl p-6 space-y-4">
            <h3 className="font-semibold text-foreground">Exportar</h3>
            
            <div className="space-y-3">
              <div className="flex items-center justify-between p-3 rounded-lg bg-secondary">
                <span className="text-sm text-foreground">Formato</span>
                <span className="text-sm font-medium text-muted-foreground">MP4</span>
              </div>
              <div className="flex items-center justify-between p-3 rounded-lg bg-secondary">
                <span className="text-sm text-foreground">Resolución</span>
                <span className="text-sm font-medium text-muted-foreground">1080p</span>
              </div>
              <div className="flex items-center justify-between p-3 rounded-lg bg-secondary">
                <span className="text-sm text-foreground">Duración</span>
                <span className="text-sm font-medium text-muted-foreground">0:54</span>
              </div>
            </div>

            <Button className="w-full gradient-accent shadow-accent-glow">
              <Download className="w-4 h-4 mr-2" />
              Exportar Short
            </Button>
          </div>

          {/* AI Suggestions */}
          <div className="glass rounded-2xl p-6 space-y-4">
            <div className="flex items-center gap-2">
              <Sparkles className="w-5 h-5 text-accent" />
              <h3 className="font-semibold text-foreground">Sugerencias IA</h3>
            </div>
            
            <div className="space-y-3">
              <div className="p-3 rounded-lg bg-accent/10 border border-accent/20">
                <p className="text-sm text-foreground mb-2">
                  "Los primeros 15 segundos tienen alto engagement"
                </p>
                <Button variant="ghost" size="sm" className="text-accent">
                  Usar este segmento
                </Button>
              </div>
              <div className="p-3 rounded-lg bg-primary/10 border border-primary/20">
                <p className="text-sm text-foreground mb-2">
                  "Momento destacado en 1:23 - 1:45"
                </p>
                <Button variant="ghost" size="sm" className="text-primary">
                  Usar este segmento
                </Button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </MainLayout>
  );
};

export default Editor;
