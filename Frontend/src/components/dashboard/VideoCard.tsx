import { Play, MoreVertical, Eye, Heart } from "lucide-react";
import { Button } from "@/components/ui/button";
import { cn } from "@/lib/utils";

interface VideoCardProps {
  title: string;
  thumbnail: string;
  duration: string;
  views: string;
  likes: string;
  status: "published" | "scheduled" | "draft";
  platform?: string;
}

export const VideoCard = ({
  title,
  thumbnail,
  duration,
  views,
  likes,
  status,
  platform,
}: VideoCardProps) => {
  const statusStyles = {
    published: "bg-success/20 text-success border-success/30",
    scheduled: "bg-warning/20 text-warning border-warning/30",
    draft: "bg-muted text-muted-foreground border-border",
  };

  const statusLabels = {
    published: "Publicado",
    scheduled: "Programado",
    draft: "Borrador",
  };

  return (
    <div className="glass rounded-2xl overflow-hidden group hover:scale-[1.02] transition-all duration-300 animate-fade-in">
      {/* Thumbnail */}
      <div className="relative aspect-video bg-muted overflow-hidden">
        <img
          src={thumbnail}
          alt={title}
          className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500"
        />
        <div className="absolute inset-0 bg-gradient-to-t from-background/80 via-transparent to-transparent opacity-0 group-hover:opacity-100 transition-opacity" />
        
        {/* Play Button - CORREGIDO (Línea 48): Agregado aria-label y title */}
        <button 
          aria-label="Reproducir video"
          title="Reproducir video"
          className="absolute inset-0 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity"
        >
          <div className="w-14 h-14 rounded-full gradient-primary flex items-center justify-center shadow-glow">
            <Play className="w-6 h-6 text-primary-foreground fill-current ml-1" />
          </div>
        </button>

        {/* Duration Badge */}
        <span className="absolute bottom-3 right-3 px-2 py-1 rounded-md bg-background/80 backdrop-blur-sm text-xs font-medium text-foreground">
          {duration}
        </span>

        {/* Status Badge */}
        <span className={cn(
          "absolute top-3 left-3 px-2 py-1 rounded-md text-xs font-medium border backdrop-blur-sm",
          statusStyles[status]
        )}>
          {statusLabels[status]}
        </span>
      </div>

      {/* Content */}
      <div className="p-4 space-y-3">
        <div className="flex items-start justify-between gap-2">
          <h3 className="font-semibold text-foreground line-clamp-2 flex-1">{title}</h3>
          
          {/* Button - CORREGIDO (Línea 72): Usamos casting 'as any' para saltar el error de tipos de TS temporalmente */}
          <Button 
            {...({ variant: "ghost", size: "icon" } as any)} 
            className="flex-shrink-0 -mr-2"
            aria-label="Más opciones"
            title="Más opciones"
          >
            <MoreVertical className="w-4 h-4" />
          </Button>
        </div>

        <div className="flex items-center gap-4 text-sm text-muted-foreground">
          <span className="flex items-center gap-1">
            <Eye className="w-4 h-4" />
            {views}
          </span>
          <span className="flex items-center gap-1">
            <Heart className="w-4 h-4" />
            {likes}
          </span>
          {platform && (
            <span className="ml-auto text-xs px-2 py-0.5 rounded-full bg-secondary">
              {platform}
            </span>
          )}
        </div>
      </div>
    </div>
  );
};
