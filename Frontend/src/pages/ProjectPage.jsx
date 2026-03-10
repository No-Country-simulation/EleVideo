import { useState, useRef } from 'react';
import { useParams, Link } from 'react-router-dom';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { projectsApi } from '@/api/projects';
import { videosApi } from '@/api/videos';
import { Layout } from '@/components/Layout';
import { VideoPreviewModal } from '@/components/VideoPreviewModal';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle, DialogTrigger } from '@/components/ui/dialog';
import { Skeleton } from '@/components/ui/skeleton';
import { Badge } from '@/components/ui/badge';
import { Progress } from '@/components/ui/progress';
import { toast } from 'sonner';
import {
  ArrowLeft,
  UploadCloud,
  Film,
  MoreVertical,
  Trash2,
  Loader2,
  Play,
  Wand2,
  FileVideo,
  Clock,
  HardDrive,
  Sparkles,
  Eye,
} from 'lucide-react';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from '@/components/ui/alert-dialog';

const statusConfig = {
  UPLOADED: { 
    label: 'Listo', 
    className: 'bg-green-500/10 text-green-600 border-green-500/20 dark:text-green-400',
    icon: '✓'
  },
  PROCESSING: { 
    label: 'Procesando', 
    className: 'bg-blue-500/10 text-blue-600 border-blue-500/20 dark:text-blue-400 status-processing',
    icon: '◌'
  },
  READY: { 
    label: 'Completado', 
    className: 'bg-purple-500/10 text-purple-600 border-purple-500/20 dark:text-purple-400',
    icon: '★'
  },
  FAILED: { 
    label: 'Error', 
    className: 'bg-red-500/10 text-red-600 border-red-500/20 dark:text-red-400',
    icon: '✕'
  },
};

function formatDuration(seconds) {
  if (!seconds) return '--:--';
  const totalSeconds = Math.floor(seconds);
  const minutes = Math.floor(totalSeconds / 60);
  const secs = totalSeconds % 60;
  return `${minutes}:${secs.toString().padStart(2, '0')}`;
}

function formatFileSize(bytes) {
  if (!bytes) return '--';
  const mb = bytes / (1024 * 1024);
  return `${mb.toFixed(1)} MB`;
}

export function ProjectPage() {
  const { projectId } = useParams();
  const [isUploadOpen, setIsUploadOpen] = useState(false);
  const [isDeleteOpen, setIsDeleteOpen] = useState(false);
  const [isPreviewOpen, setIsPreviewOpen] = useState(false);
  const [selectedVideo, setSelectedVideo] = useState(null);
  const [videoTitle, setVideoTitle] = useState('');
  const [videoFile, setVideoFile] = useState(null);
  const [uploadProgress, setUploadProgress] = useState(0);
  const [isDragging, setIsDragging] = useState(false);
  const fileInputRef = useRef(null);
  const queryClient = useQueryClient();

  const { data: projectData, isLoading: projectLoading } = useQuery({
    queryKey: ['project', projectId],
    queryFn: () => projectsApi.getById(projectId),
  });

  const { data: videosData, isLoading: videosLoading } = useQuery({
    queryKey: ['videos', projectId],
    queryFn: () => videosApi.getByProject(projectId, { page: 0, size: 50 }),
  });

  const uploadMutation = useMutation({
    mutationFn: (formData) => videosApi.create(projectId, formData),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['videos', projectId] });
      setIsUploadOpen(false);
      setVideoTitle('');
      setVideoFile(null);
      setUploadProgress(0);
      toast.success('¡Video subido exitosamente!');
    },
    onError: (error) => {
      toast.error(error.response?.data?.message || 'Error al subir video');
      setUploadProgress(0);
    },
  });

  const deleteMutation = useMutation({
    mutationFn: (videoId) => videosApi.delete(projectId, videoId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['videos', projectId] });
      setIsDeleteOpen(false);
      setSelectedVideo(null);
      toast.success('Video eliminado');
    },
    onError: (error) => {
      toast.error(error.response?.data?.message || 'Error al eliminar');
    },
  });

  const handleUpload = async (e) => {
    e.preventDefault();
    if (!videoFile || !videoTitle) return;

    const formData = new FormData();
    formData.append('title', videoTitle);
    formData.append('video', videoFile);

    setUploadProgress(10);
    const progressInterval = setInterval(() => {
      setUploadProgress((prev) => Math.min(prev + 10, 90));
    }, 500);

    try {
      await uploadMutation.mutateAsync(formData);
      setUploadProgress(100);
    } finally {
      clearInterval(progressInterval);
    }
  };

  const handleFileChange = (e) => {
    const file = e.target.files?.[0];
    if (file) {
      setVideoFile(file);
      if (!videoTitle) {
        setVideoTitle(file.name.replace(/\.[^/.]+$/, ''));
      }
    }
  };

  const handleDrop = (e) => {
    e.preventDefault();
    setIsDragging(false);
    const file = e.dataTransfer.files?.[0];
    if (file && file.type.startsWith('video/')) {
      setVideoFile(file);
      if (!videoTitle) {
        setVideoTitle(file.name.replace(/\.[^/.]+$/, ''));
      }
    }
  };

  const project = projectData?.data || projectData;
  const videos = videosData?.data?.content || videosData?.content || [];

  return (
    <Layout>
      <div className="space-y-8" data-testid="project-page">
        {/* Header */}
        <div className="space-y-4">
          <Link
            to="/dashboard"
            className="inline-flex items-center gap-2 text-sm text-muted-foreground hover:text-foreground transition-colors group"
          >
            <ArrowLeft className="h-4 w-4 group-hover:-translate-x-1 transition-transform" />
            Volver a proyectos
          </Link>
          <div className="flex flex-col sm:flex-row sm:items-end sm:justify-between gap-6">
            <div className="space-y-1">
              {projectLoading ? (
                <Skeleton className="h-10 w-64" />
              ) : (
                <>
                  <h1 className="font-outfit text-4xl font-bold tracking-tight">
                    {project?.name}
                  </h1>
                  {project?.description && (
                    <p className="text-muted-foreground text-lg">{project.description}</p>
                  )}
                </>
              )}
            </div>
            <Dialog open={isUploadOpen} onOpenChange={setIsUploadOpen}>
              <DialogTrigger asChild>
                <Button 
                  size="lg"
                  className="bg-gradient-to-r from-blue-500 to-purple-600 hover:from-blue-600 hover:to-purple-700 shadow-lg shadow-purple-500/25"
                  data-testid="upload-video-button"
                >
                  <UploadCloud className="mr-2 h-5 w-5" />
                  Subir video
                </Button>
              </DialogTrigger>
              <DialogContent className="sm:max-w-lg">
                <form onSubmit={handleUpload}>
                  <DialogHeader>
                    <DialogTitle className="font-outfit text-xl">Subir video</DialogTitle>
                    <DialogDescription>
                      Sube un video horizontal para convertirlo a formato vertical
                    </DialogDescription>
                  </DialogHeader>
                  <div className="space-y-4 py-6">
                    <div className="space-y-2">
                      <Label htmlFor="title">Título del video</Label>
                      <Input
                        id="title"
                        value={videoTitle}
                        onChange={(e) => setVideoTitle(e.target.value)}
                        placeholder="Ej: Tutorial de marketing"
                        className="h-11"
                        data-testid="video-title-input"
                        required
                      />
                    </div>
                    <div className="space-y-2">
                      <Label>Archivo de video</Label>
                      <div
                        className={`relative border-2 border-dashed rounded-xl p-8 text-center cursor-pointer transition-all ${
                          isDragging 
                            ? 'border-purple-500 bg-purple-500/10' 
                            : 'border-border hover:border-purple-500/50 hover:bg-muted/50'
                        }`}
                        onClick={() => fileInputRef.current?.click()}
                        onDragOver={(e) => { e.preventDefault(); setIsDragging(true); }}
                        onDragLeave={() => setIsDragging(false)}
                        onDrop={handleDrop}
                      >
                        <input
                          ref={fileInputRef}
                          type="file"
                          accept="video/*"
                          onChange={handleFileChange}
                          className="hidden"
                          data-testid="video-file-input"
                        />
                        {videoFile ? (
                          <div className="space-y-3">
                            <div className="w-16 h-16 mx-auto rounded-xl bg-gradient-to-br from-blue-500/20 to-purple-500/20 flex items-center justify-center">
                              <FileVideo className="h-8 w-8 text-purple-500" />
                            </div>
                            <div>
                              <p className="font-medium truncate max-w-xs mx-auto">{videoFile.name}</p>
                              <p className="text-sm text-muted-foreground mt-1">
                                {formatFileSize(videoFile.size)}
                              </p>
                            </div>
                            <Button 
                              type="button" 
                              variant="ghost" 
                              size="sm"
                              onClick={(e) => { e.stopPropagation(); setVideoFile(null); }}
                            >
                              Cambiar archivo
                            </Button>
                          </div>
                        ) : (
                          <div className="space-y-3">
                            <div className="w-16 h-16 mx-auto rounded-xl bg-muted flex items-center justify-center">
                              <UploadCloud className="h-8 w-8 text-muted-foreground" />
                            </div>
                            <div>
                              <p className="font-medium">
                                Arrastra tu video aquí o haz clic
                              </p>
                              <p className="text-sm text-muted-foreground mt-1">
                                MP4, MOV, AVI, WebM (máx. 200MB)
                              </p>
                            </div>
                          </div>
                        )}
                      </div>
                    </div>
                    {uploadProgress > 0 && (
                      <div className="space-y-2">
                        <div className="flex justify-between text-sm">
                          <span className="text-muted-foreground">Subiendo...</span>
                          <span className="font-medium">{uploadProgress}%</span>
                        </div>
                        <Progress value={uploadProgress} className="h-2" />
                      </div>
                    )}
                  </div>
                  <DialogFooter>
                    <Button
                      type="submit"
                      className="w-full bg-gradient-to-r from-blue-500 to-purple-600 hover:from-blue-600 hover:to-purple-700"
                      disabled={!videoFile || !videoTitle || uploadMutation.isPending}
                      data-testid="upload-video-submit"
                    >
                      {uploadMutation.isPending ? (
                        <Loader2 className="mr-2 h-5 w-5 animate-spin" />
                      ) : (
                        <UploadCloud className="mr-2 h-5 w-5" />
                      )}
                      Subir video
                    </Button>
                  </DialogFooter>
                </form>
              </DialogContent>
            </Dialog>
          </div>
        </div>

        {/* Videos Grid */}
        {videosLoading ? (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {[...Array(6)].map((_, i) => (
              <Card key={i} className="overflow-hidden">
                <Skeleton className="aspect-video w-full" />
                <CardHeader>
                  <Skeleton className="h-5 w-3/4" />
                  <Skeleton className="h-4 w-1/2 mt-2" />
                </CardHeader>
              </Card>
            ))}
          </div>
        ) : videos.length === 0 ? (
          <div className="relative">
            <div className="absolute inset-0 empty-state-bg rounded-3xl" />
            <Card className="relative text-center py-20 border-dashed border-2 bg-transparent">
              <CardContent className="space-y-6">
                <div className="relative inline-flex">
                  <div className="absolute inset-0 bg-gradient-to-r from-blue-500 to-purple-500 rounded-full blur-xl opacity-30 animate-pulse" />
                  <div className="relative p-6 rounded-full bg-gradient-to-br from-blue-500/10 to-purple-500/10 border border-purple-500/20">
                    <Film className="h-16 w-16 text-purple-500" />
                  </div>
                </div>
                <div className="space-y-2">
                  <h3 className="font-outfit text-2xl font-semibold">
                    Sube tu primer video
                  </h3>
                  <p className="text-muted-foreground max-w-sm mx-auto">
                    Sube un video horizontal y conviértelo automáticamente a formato vertical para TikTok, Instagram Reels y YouTube Shorts
                  </p>
                </div>
                <Button 
                  size="lg"
                  className="bg-gradient-to-r from-blue-500 to-purple-600 hover:from-blue-600 hover:to-purple-700 shadow-lg"
                  onClick={() => setIsUploadOpen(true)}
                >
                  <Sparkles className="mr-2 h-5 w-5" />
                  Subir mi primer video
                </Button>
              </CardContent>
            </Card>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {videos.map((video) => {
              const status = statusConfig[video.status] || statusConfig.UPLOADED;
              
              return (
                <Card 
                  key={video.id} 
                  className="video-card card-3d overflow-hidden border-border/50 bg-card/50 backdrop-blur-sm group"
                  data-testid={`video-card-${video.id}`}
                >
                  {/* Thumbnail */}
                  <div className="relative aspect-video bg-gradient-to-br from-slate-800 to-slate-900 overflow-hidden">
                    {video.thumbnailUrl || video.videoUrl ? (
                      <>
                        {video.thumbnailUrl ? (
                          <img
                            src={video.thumbnailUrl}
                            alt={video.title}
                            className="w-full h-full object-cover"
                          />
                        ) : (
                          <video
                            src={video.videoUrl}
                            className="w-full h-full object-cover"
                            muted
                            preload="metadata"
                          />
                        )}
                      </>
                    ) : (
                      <div className="w-full h-full flex items-center justify-center bg-gradient-to-br from-blue-500/20 to-purple-500/20">
                        <Film className="h-12 w-12 text-white/50" />
                      </div>
                    )}
                    
                    {/* Overlay */}
                    <div className="absolute inset-0 bg-gradient-to-t from-black/80 via-black/20 to-transparent opacity-0 group-hover:opacity-100 transition-opacity" />
                    
                    {/* Play button */}
                    <div className="absolute inset-0 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-all">
                      <Button
                        size="icon"
                        className="w-14 h-14 rounded-full bg-white/90 hover:bg-white text-black shadow-xl hover:scale-110 transition-transform"
                        onClick={(e) => {
                          e.preventDefault();
                          setSelectedVideo(video);
                          setIsPreviewOpen(true);
                        }}
                      >
                        <Play className="h-6 w-6 ml-1" fill="currentColor" />
                      </Button>
                    </div>

                    {/* Duration badge */}
                    {video.durationInSeconds && (
                      <div className="absolute bottom-2 right-2 px-2 py-1 rounded-md bg-black/70 text-white text-xs font-medium backdrop-blur-sm">
                        {formatDuration(video.durationInSeconds)}
                      </div>
                    )}

                    {/* Status badge */}
                    <div className="absolute top-2 left-2">
                      <Badge className={`${status.className} border font-medium`}>
                        {status.icon} {status.label}
                      </Badge>
                    </div>

                    {/* Menu */}
                    <div className="absolute top-2 right-2 opacity-0 group-hover:opacity-100 transition-opacity">
                      <DropdownMenu>
                        <DropdownMenuTrigger asChild>
                          <Button 
                            variant="ghost" 
                            size="icon" 
                            className="h-8 w-8 bg-black/50 hover:bg-black/70 text-white"
                          >
                            <MoreVertical className="h-4 w-4" />
                          </Button>
                        </DropdownMenuTrigger>
                        <DropdownMenuContent align="end">
                          <DropdownMenuItem onClick={() => {
                            setSelectedVideo(video);
                            setIsPreviewOpen(true);
                          }}>
                            <Eye className="mr-2 h-4 w-4" />
                            Ver video
                          </DropdownMenuItem>
                          <DropdownMenuItem asChild>
                            <Link to={`/projects/${projectId}/videos/${video.id}`}>
                              <Wand2 className="mr-2 h-4 w-4" />
                              Procesar
                            </Link>
                          </DropdownMenuItem>
                          <DropdownMenuItem
                            onClick={() => {
                              setSelectedVideo(video);
                              setIsDeleteOpen(true);
                            }}
                            className="text-destructive focus:text-destructive"
                          >
                            <Trash2 className="mr-2 h-4 w-4" />
                            Eliminar
                          </DropdownMenuItem>
                        </DropdownMenuContent>
                      </DropdownMenu>
                    </div>
                  </div>

                  {/* Content */}
                  <CardHeader className="space-y-3">
                    <CardTitle className="font-outfit text-base line-clamp-1 group-hover:text-purple-500 transition-colors">
                      <Link to={`/projects/${projectId}/videos/${video.id}`}>
                        {video.title}
                      </Link>
                    </CardTitle>
                    <div className="flex items-center gap-4 text-xs text-muted-foreground">
                      {video.width && video.height && (
                        <span className="flex items-center gap-1">
                          <span className="font-medium">{video.width}×{video.height}</span>
                        </span>
                      )}
                      {video.sizeInBytes && (
                        <span className="flex items-center gap-1">
                          <HardDrive className="h-3 w-3" />
                          {formatFileSize(video.sizeInBytes)}
                        </span>
                      )}
                    </div>
                    <Link 
                      to={`/projects/${projectId}/videos/${video.id}`}
                      className="inline-flex"
                    >
                      <Button 
                        size="sm" 
                        className="w-full bg-gradient-to-r from-blue-500 to-purple-600 hover:from-blue-600 hover:to-purple-700"
                      >
                        <Wand2 className="mr-2 h-4 w-4" />
                        Convertir a vertical
                      </Button>
                    </Link>
                  </CardHeader>
                </Card>
              );
            })}
          </div>
        )}

        {/* Video Preview Modal */}
        <VideoPreviewModal
          isOpen={isPreviewOpen}
          onClose={() => setIsPreviewOpen(false)}
          video={selectedVideo}
        />

        {/* Delete Alert */}
        <AlertDialog open={isDeleteOpen} onOpenChange={setIsDeleteOpen}>
          <AlertDialogContent>
            <AlertDialogHeader>
              <AlertDialogTitle>¿Eliminar video?</AlertDialogTitle>
              <AlertDialogDescription>
                Esta acción no se puede deshacer. Se eliminará el video y todos sus procesamientos.
              </AlertDialogDescription>
            </AlertDialogHeader>
            <AlertDialogFooter>
              <AlertDialogCancel>Cancelar</AlertDialogCancel>
              <AlertDialogAction
                onClick={() => deleteMutation.mutate(selectedVideo?.id)}
                className="bg-destructive hover:bg-destructive/90"
                data-testid="confirm-delete-video"
              >
                {deleteMutation.isPending && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
                Eliminar
              </AlertDialogAction>
            </AlertDialogFooter>
          </AlertDialogContent>
        </AlertDialog>
      </div>
    </Layout>
  );
}
