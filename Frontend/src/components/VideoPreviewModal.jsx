import { Dialog, DialogContent } from '@/components/ui/dialog';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { X, Download, Clock, Scissors } from 'lucide-react';

function formatTime(seconds) {
  if (seconds === null || seconds === undefined) return '--:--';
  const mins = Math.floor(seconds / 60);
  const secs = Math.floor(seconds % 60);
  return `${mins}:${secs.toString().padStart(2, '0')}`;
}

// Platform icons
const TikTokIcon = ({ className }) => (
  <svg className={className} viewBox="0 0 24 24" fill="currentColor">
    <path d="M19.59 6.69a4.83 4.83 0 0 1-3.77-4.25V2h-3.45v13.67a2.89 2.89 0 0 1-5.2 1.74 2.89 2.89 0 0 1 2.31-4.64 2.93 2.93 0 0 1 .88.13V9.4a6.84 6.84 0 0 0-1-.05A6.33 6.33 0 0 0 5 20.1a6.34 6.34 0 0 0 10.86-4.43v-7a8.16 8.16 0 0 0 4.77 1.52v-3.4a4.85 4.85 0 0 1-1-.1z"/>
  </svg>
);

const InstagramIcon = ({ className }) => (
  <svg className={className} viewBox="0 0 24 24" fill="currentColor">
    <path d="M12 2.163c3.204 0 3.584.012 4.85.07 3.252.148 4.771 1.691 4.919 4.919.058 1.265.069 1.645.069 4.849 0 3.205-.012 3.584-.069 4.849-.149 3.225-1.664 4.771-4.919 4.919-1.266.058-1.644.07-4.85.07-3.204 0-3.584-.012-4.849-.07-3.26-.149-4.771-1.699-4.919-4.92-.058-1.265-.07-1.644-.07-4.849 0-3.204.013-3.583.07-4.849.149-3.227 1.664-4.771 4.919-4.919 1.266-.057 1.645-.069 4.849-.069zm0-2.163c-3.259 0-3.667.014-4.947.072-4.358.2-6.78 2.618-6.98 6.98-.059 1.281-.073 1.689-.073 4.948 0 3.259.014 3.668.072 4.948.2 4.358 2.618 6.78 6.98 6.98 1.281.058 1.689.072 4.948.072 3.259 0 3.668-.014 4.948-.072 4.354-.2 6.782-2.618 6.979-6.98.059-1.28.073-1.689.073-4.948 0-3.259-.014-3.667-.072-4.947-.196-4.354-2.617-6.78-6.979-6.98-1.281-.059-1.69-.073-4.949-.073zm0 5.838c-3.403 0-6.162 2.759-6.162 6.162s2.759 6.163 6.162 6.163 6.162-2.759 6.162-6.163c0-3.403-2.759-6.162-6.162-6.162zm0 10.162c-2.209 0-4-1.79-4-4 0-2.209 1.791-4 4-4s4 1.791 4 4c0 2.21-1.791 4-4 4zm6.406-11.845c-.796 0-1.441.645-1.441 1.44s.645 1.44 1.441 1.44c.795 0 1.439-.645 1.439-1.44s-.644-1.44-1.439-1.44z"/>
  </svg>
);

const YouTubeIcon = ({ className }) => (
  <svg className={className} viewBox="0 0 24 24" fill="currentColor">
    <path d="M23.498 6.186a3.016 3.016 0 0 0-2.122-2.136C19.505 3.545 12 3.545 12 3.545s-7.505 0-9.377.505A3.017 3.017 0 0 0 .502 6.186C0 8.07 0 12 0 12s0 3.93.502 5.814a3.016 3.016 0 0 0 2.122 2.136c1.871.505 9.376.505 9.376.505s7.505 0 9.377-.505a3.015 3.015 0 0 0 2.122-2.136C24 15.93 24 12 24 12s0-3.93-.502-5.814zM9.545 15.568V8.432L15.818 12l-6.273 3.568z"/>
  </svg>
);

const getPlatformIcon = (platform) => {
  switch (platform) {
    case 'tiktok':
      return <TikTokIcon className="h-4 w-4" />;
    case 'instagram':
      return <InstagramIcon className="h-4 w-4" />;
    case 'youtube_shorts':
      return <YouTubeIcon className="h-4 w-4" />;
    default:
      return null;
  }
};

const getPlatformLabel = (platform) => {
  switch (platform) {
    case 'tiktok':
      return 'TikTok';
    case 'instagram':
      return 'Instagram';
    case 'youtube_shorts':
      return 'YouTube';
    default:
      return platform;
  }
};

export function VideoPreviewModal({ isOpen, onClose, video, rendition }) {
  const videoSrc = rendition?.outputUrl || rendition?.previewUrl || video?.videoUrl;
  const title = rendition ? `${video?.title}` : video?.title;
  const isVertical = !!rendition;

  if (!videoSrc) return null;

  const segmentStart = rendition?.segmentStart;
  const segmentDuration = rendition?.segmentDuration;
  const segmentEnd = (segmentStart !== undefined && segmentDuration) 
    ? segmentStart + segmentDuration 
    : null;

  const isShort = rendition?.processingMode?.includes('short');

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className={`p-0 overflow-hidden border-0 bg-transparent shadow-none ${isVertical ? 'max-w-xs' : 'max-w-4xl'}`}>
        {isVertical ? (
          /* Phone Frame para videos verticales - Compacto */
          <div className="relative flex flex-col items-center gap-3">
            {/* Header compacto */}
            <div className="flex items-center justify-between w-full">
              <div className="flex-1 min-w-0">
                <h3 className="font-outfit font-semibold text-white text-base truncate">
                  {title}
                </h3>
                <div className="flex items-center gap-1.5 mt-0.5">
                  <Badge className="bg-gradient-to-r from-pink-500 to-purple-500 text-white border-0 text-[10px] px-2 py-0 h-5 capitalize">
                    {rendition?.platform}
                  </Badge>
                  <Badge variant="outline" className="border-white/30 text-white/80 text-[10px] px-2 py-0 h-5">
                    {rendition?.processingMode?.replace('_', ' ')}
                  </Badge>
                </div>
              </div>
              <Button
                variant="ghost"
                size="icon"
                onClick={onClose}
                className="text-white/70 hover:text-white hover:bg-white/10 rounded-full h-8 w-8 flex-shrink-0"
              >
                <X className="h-4 w-4" />
              </Button>
            </div>

            {/* Info del segmento para shorts - compacto */}
            {isShort && segmentStart !== undefined && (
              <div className="flex items-center gap-2 px-3 py-1.5 rounded-full bg-white/10 backdrop-blur-sm text-xs">
                <Scissors className="h-3 w-3 text-purple-400" />
                <span className="text-white/90">
                  {formatTime(segmentStart)} → {formatTime(segmentEnd)}
                </span>
                <span className="text-white/40">|</span>
                <Clock className="h-3 w-3 text-blue-400" />
                <span className="text-white/90">{segmentDuration}s</span>
              </div>
            )}

            {/* Phone device frame - Tamaño reducido */}
            <div className="relative">
              <div className="relative bg-gradient-to-b from-zinc-700 via-zinc-800 to-zinc-900 rounded-[2rem] p-1 shadow-2xl shadow-black/60">
                <div className="absolute inset-0 rounded-[2rem] bg-gradient-to-tr from-white/10 via-transparent to-transparent pointer-events-none" />
                
                <div className="relative bg-black rounded-[1.8rem] overflow-hidden">
                  {/* Notch pequeño */}
                  <div className="absolute top-1.5 left-1/2 -translate-x-1/2 z-20">
                    <div className="w-16 h-4 bg-black rounded-full flex items-center justify-center gap-1.5 border border-zinc-800/50">
                      <div className="w-1 h-1 rounded-full bg-zinc-700" />
                      <div className="w-2 h-2 rounded-full bg-zinc-800 ring-1 ring-zinc-700/50" />
                    </div>
                  </div>

                  {/* Video container - reducido */}
                  <div className="relative w-52 h-[420px] bg-black">
                    <video
                      src={videoSrc}
                      controls
                      autoPlay
                      playsInline
                      className="w-full h-full object-cover"
                      data-testid="video-preview-player"
                    />
                  </div>

                  {/* Home indicator */}
                  <div className="absolute bottom-1 left-0 right-0 flex justify-center pointer-events-none">
                    <div className="w-24 h-0.5 bg-white/20 rounded-full" />
                  </div>
                </div>
              </div>

              {/* Botones laterales más pequeños */}
              <div className="absolute -left-0.5 top-20 w-0.5 h-5 bg-zinc-600 rounded-l-sm" />
              <div className="absolute -left-0.5 top-28 w-0.5 h-10 bg-zinc-600 rounded-l-sm" />
              <div className="absolute -left-0.5 top-40 w-0.5 h-10 bg-zinc-600 rounded-l-sm" />
              <div className="absolute -right-0.5 top-28 w-0.5 h-12 bg-zinc-600 rounded-r-sm" />
            </div>

            {/* Botón de descarga con icono de plataforma */}
            {rendition?.outputUrl && (
              <Button
                asChild
                className="w-52 bg-gradient-to-r from-indigo-500 via-purple-500 to-pink-500 hover:from-indigo-600 hover:via-purple-600 hover:to-pink-600 text-white border-0 shadow-lg shadow-purple-500/30 hover:shadow-purple-500/50 transition-all h-9 text-sm"
              >
                <a
                  href={rendition.outputUrl}
                  download
                  target="_blank"
                  rel="noopener noreferrer"
                  className="flex items-center justify-center gap-2 w-full"
                >
                  {getPlatformIcon(rendition?.platform)}

                  <span className="truncate">
                    Descargar para {getPlatformLabel(rendition?.platform)}
                  </span>

                  <Download className="h-3.5 w-3.5" />
                </a>
              </Button>
            )}

            {/* Info adicional compacta */}
            <div className="text-[10px] text-white/40 text-center">
              {rendition?.quality} • {rendition?.backgroundMode?.replace('_', ' ')}
            </div>
          </div>
        ) : (
          /* Standard player para videos horizontales */
          <div className="relative bg-black rounded-2xl overflow-hidden">
            <div className="absolute top-0 left-0 right-0 z-10 p-4 bg-gradient-to-b from-black/80 to-transparent">
              <div className="flex items-center justify-between">
                <h3 className="font-outfit font-semibold text-white truncate max-w-md">
                  {title}
                </h3>
                <Button
                  variant="ghost"
                  size="icon"
                  onClick={onClose}
                  className="text-white hover:bg-white/20"
                >
                  <X className="h-5 w-5" />
                </Button>
              </div>
            </div>

            <video
              src={videoSrc}
              controls
              autoPlay
              className="w-full max-h-[75vh] object-contain"
              data-testid="video-preview-player"
            />
          </div>
        )}
      </DialogContent>
    </Dialog>
  );
}

export { TikTokIcon, InstagramIcon, YouTubeIcon };
