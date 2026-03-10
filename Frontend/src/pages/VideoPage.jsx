import { useState, useEffect, useRef } from 'react';
import { useParams, Link } from 'react-router-dom';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { videosApi } from '@/api/videos';
import { processingApi } from '@/api/processing';
import { Layout } from '@/components/Layout';
import { VideoPreviewModal, TikTokIcon, InstagramIcon, YouTubeIcon } from '@/components/VideoPreviewModal';
import { notifyProcessingComplete, requestNotificationPermission } from '@/lib/notifications';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import { Slider } from '@/components/ui/slider';
import { Switch } from '@/components/ui/switch';
import { Badge } from '@/components/ui/badge';
import { Skeleton } from '@/components/ui/skeleton';
import { Progress } from '@/components/ui/progress';
import { toast } from 'sonner';
import {
  ArrowLeft,
  Wand2,
  Loader2,
  Download,
  Trash2,
  Clock,
  XCircle,
  Play,
  Film,
  Smartphone,
  Settings2,
  Sparkles,
  Eye,
  CheckCircle,
  AlertCircle,
  RefreshCw,
  Scissors,
} from 'lucide-react';
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

const platformOptions = [
  { value: 'tiktok', label: 'TikTok', color: 'text-pink-500' },
  { value: 'instagram', label: 'Instagram Reels', color: 'text-purple-500' },
  { value: 'youtube_shorts', label: 'YouTube Shorts', color: 'text-red-500' },
];

const platformBadgeStyles = {
  tiktok: {
    label: "TikTok",
    className: "bg-black text-white border-0"
  },
  instagram: {
    label: "Instagram",
    className: "bg-gradient-to-r from-pink-500 via-purple-500 to-orange-500 text-white border-0"
  },
  instagram_reels: {
    label: "Reels",
    className: "bg-gradient-to-r from-pink-500 via-purple-500 to-orange-500 text-white border-0"
  },
  youtube_shorts: {
    label: "Shorts",
    className: "bg-red-600 text-white border-0"
  }
};

const qualityOptions = [
  { value: 'fast', label: 'Rápido', desc: 'Menor calidad, más rápido' },
  { value: 'normal', label: 'Normal', desc: 'Balance óptimo' },
  { value: 'high', label: 'Alta calidad', desc: 'Mejor calidad, más lento' },
];

const backgroundModeOptions = [
  { value: 'smart_crop', label: 'Recorte inteligente', desc: 'IA detecta el sujeto principal' },
  { value: 'blurred', label: 'Fondo difuminado', desc: 'Video original como fondo blur' },
  { value: 'black', label: 'Barras negras', desc: 'Fondo negro simple' },
];

const jobStatusConfig = {
  pending: { label: 'En cola', icon: Clock, className: 'bg-yellow-500/10 text-yellow-600 border-yellow-500/20' },
  processing: { label: 'Procesando', icon: RefreshCw, className: 'bg-blue-500/10 text-blue-600 border-blue-500/20' },
  completed: { label: 'Completado', icon: CheckCircle, className: 'bg-green-500/10 text-green-600 border-green-500/20' },
  failed: { label: 'Error', icon: AlertCircle, className: 'bg-red-500/10 text-red-600 border-red-500/20' },
  cancelled: { label: 'Cancelado', icon: XCircle, className: 'bg-gray-500/10 text-gray-600 border-gray-500/20' },
};


const processingModeLabels = {
  vertical: "Video completo",
  short_auto: "Clip automático",
  short_manual: "Clip manual"
};

const qualityLabels = {
  fast: "⚡ Rápido",
  normal: "⚖ Balance",
  high: "⭐ Alta"
};

const backgroundLabels = {
  smart_crop: "Recorte IA",
  blurred: "Fondo blur",
  black: "Fondo negro"
};

function timeAgo(dateStr){
  if(!dateStr) return "";
  const diff = (Date.now() - new Date(dateStr).getTime())/1000;
  const m = Math.floor(diff/60);
  const h = Math.floor(diff/3600);
  const d = Math.floor(diff/86400);
  if(m < 1) return "ahora";
  if(m < 60) return `hace ${m} min`;
  if(h < 24) return `hace ${h} h`;
  return `hace ${d} d`;
}

function formatDuration(seconds) {
  if (!seconds) return '--:--';
  const totalSeconds = Math.floor(seconds);
  const minutes = Math.floor(totalSeconds / 60);
  const secs = totalSeconds % 60;
  return `${minutes}:${secs.toString().padStart(2, '0')}`;
}

export function VideoPage() {
  const { projectId, videoId } = useParams();
  const queryClient = useQueryClient();
  const prevJobsRef = useRef([]);
  
  // Processing form state
  const [processingMode, setProcessingMode] = useState('vertical');
  const [platform, setPlatform] = useState('tiktok');
  const [quality, setQuality] = useState('normal');
  const [backgroundMode, setBackgroundMode] = useState('smart_crop');
  const [shortAutoDuration, setShortAutoDuration] = useState(30);
  const [shortStartTime, setShortStartTime] = useState(0);
  const [shortDuration, setShortDuration] = useState(30);
  const [showAdvanced, setShowAdvanced] = useState(false);
  const [headroomRatio, setHeadroomRatio] = useState(0.15);
  const [smoothingStrength, setSmoothingStrength] = useState(0.75);
  
  const [isDeleteRenditionOpen, setIsDeleteRenditionOpen] = useState(false);
  const [selectedRendition, setSelectedRendition] = useState(null);
  const [isPreviewOpen, setIsPreviewOpen] = useState(false);
  const [previewVideo, setPreviewVideo] = useState(null);
  const [previewRendition, setPreviewRendition] = useState(null);

  const { data: videoData, isLoading: videoLoading } = useQuery({
    queryKey: ['video', projectId, videoId],
    queryFn: () => videosApi.getById(projectId, videoId),
  });

  const { data: jobsData, isLoading: jobsLoading } = useQuery({
    queryKey: ['jobs', projectId, videoId],
    queryFn: () => processingApi.getJobs(projectId, videoId, { page: 0, size: 20 }),
    refetchInterval: 5000,
  });

  const { data: renditionsData, isLoading: renditionsLoading, refetch: refetchRenditions } = useQuery({
    queryKey: ['renditions', projectId, videoId],
    queryFn: () => processingApi.getRenditions(projectId, videoId, { page: 0, size: 20 }),
  });

  // Check for completed jobs and notify
  useEffect(() => {
  const jobs = jobsData?.data?.content || jobsData?.content || [];
  const prevJobs = prevJobsRef.current;

  jobs.forEach((job) => {
    const prevJob = prevJobs.find((p) => (p.id || p.jobId) === (job.id || job.jobId));

    const status = job.status?.toLowerCase();
    const prevStatus = prevJob?.status?.toLowerCase();

    if (prevJob && prevStatus !== status) {
      if (status === 'completed' || status === 'failed') {
        notifyProcessingComplete(videoData?.data?.title || 'Video', status);

        if (status === 'completed') {
          refetchRenditions();
        }
      }
    }
  });

  prevJobsRef.current = jobs;
}, [jobsData, videoData, refetchRenditions]);

  // Request notification permission on mount
  useEffect(() => {
    requestNotificationPermission();
  }, []);

  const processMutation = useMutation({
    mutationFn: (data) => processingApi.createJob(projectId, videoId, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['jobs', projectId, videoId] });
      toast.success('¡Procesamiento iniciado! Te notificaremos cuando termine.');
    },
    onError: (error) => {
      toast.error(error.response?.data?.message || 'Error al iniciar procesamiento');
    },
  });

  const cancelJobMutation = useMutation({
    mutationFn: (jobId) => processingApi.cancelJob(projectId, videoId, jobId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['jobs', projectId, videoId] });
      toast.success('Job cancelado');
    },
    onError: (error) => {
      toast.error(error.response?.data?.message || 'Error al cancelar');
    },
  });

  const deleteRenditionMutation = useMutation({
    mutationFn: (renditionId) => processingApi.deleteRendition(projectId, videoId, renditionId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['renditions', projectId, videoId] });
      setIsDeleteRenditionOpen(false);
      toast.success('Rendición eliminada');
    },
    onError: (error) => {
      toast.error(error.response?.data?.message || 'Error al eliminar');
    },
  });

  const handleProcess = () => {
    const data = {
      processingMode,
      platform,
      quality,
      backgroundMode,
    };

    if (processingMode === 'short_auto') {
      data.shortAutoDuration = shortAutoDuration;
    } else if (processingMode === 'short_manual') {
      data.shortOptions = {
        startTime: shortStartTime,
        duration: shortDuration,
      };
    }

    if (showAdvanced) {
      data.advancedOptions = {
        headroomRatio,
        smoothingStrength,
      };
    }

    processMutation.mutate(data);
  };

  const video = videoData?.data || videoData;
  const jobs = jobsData?.data?.content || jobsData?.content || [];
  const renditions = renditionsData?.data?.content || renditionsData?.content || [];
  const activeJobs = jobs.filter((j) => j.status === 'pending' || j.status === 'processing');

  return (
    <Layout>
      <div className="space-y-8" data-testid="video-page">
        {/* Header */}
        <div className="space-y-4">
          <Link
            to={`/projects/${projectId}`}
            className="inline-flex items-center gap-2 text-sm text-muted-foreground hover:text-foreground transition-colors group"
          >
            <ArrowLeft className="h-4 w-4 group-hover:-translate-x-1 transition-transform" />
            Volver al proyecto
          </Link>
          {videoLoading ? (
            <Skeleton className="h-10 w-64" />
          ) : (
            <h1 className="font-outfit text-4xl font-bold tracking-tight">
              {video?.title}
            </h1>
          )}
        </div>

        {/* Main Content */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Video Player & Results */}
          <div className="lg:col-span-2 space-y-6">
            {/* Video Player */}
            {videoLoading ? (
              <Skeleton className="aspect-video w-full rounded-2xl" />
            ) : video?.videoUrl ? (
              <div className="relative aspect-video bg-black rounded-2xl overflow-hidden shadow-2xl">
                <video
                  src={video.videoUrl}
                  controls
                  className="w-full h-full"
                  data-testid="video-player"
                  poster={video.thumbnailUrl}
                />
              </div>
            ) : (
              <div className="aspect-video bg-gradient-to-br from-slate-800 to-slate-900 rounded-2xl flex items-center justify-center">
                <Film className="h-20 w-20 text-white/20" />
              </div>
            )}

            {/* Video Info */}
            {video && (
              <div className="grid grid-cols-2 sm:grid-cols-4 gap-4">
                {[
                  { label: 'Duración', value: formatDuration(video.durationInSeconds) },
                  { label: 'Resolución', value: `${video.width}×${video.height}` },
                  { label: 'Formato', value: (video.format || 'MP4').toUpperCase() },
                  { label: 'Estado', value: video.status, isStatus: true },
                ].map((item) => (
                  <div key={item.label} className="stat-card rounded-xl p-4">
                    <p className="text-sm text-muted-foreground">{item.label}</p>
                    <p className={`font-semibold font-outfit ${item.isStatus ? 'text-green-500' : ''}`}>
                      {item.value}
                    </p>
                  </div>
                ))}
              </div>
            )}

            {/* Tabs */}
            <Tabs defaultValue="renditions" className="space-y-6">
              <TabsList className="w-full grid grid-cols-2 h-12 p-1 bg-muted/50">
                <TabsTrigger value="renditions" className="data-[state=active]:bg-background" data-testid="renditions-tab">
                  <Smartphone className="mr-2 h-4 w-4" />
                  Videos procesados ({renditions.length})
                </TabsTrigger>
                <TabsTrigger value="jobs" className="data-[state=active]:bg-background" data-testid="jobs-tab">
                  <Clock className="mr-2 h-4 w-4" />
                  Jobs {activeJobs.length > 0 && `(${activeJobs.length} activos)`}
                </TabsTrigger>
              </TabsList>

              {/* Renditions Tab */}
              <TabsContent value="renditions" className="space-y-4">
                {renditionsLoading ? (
                  <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-5">
                    {[...Array(3)].map((_, i) => (
                      <Skeleton key={i} className="aspect-[9/16] rounded-xl" />
                    ))}
                  </div>
                ) : renditions.length === 0 ? (
                  <Card className="text-center py-12 border-dashed border-2">
                    <CardContent className="space-y-4">
                      <div className="w-16 h-16 mx-auto rounded-full bg-purple-500/10 flex items-center justify-center">
                        <Smartphone className="h-8 w-8 text-purple-500" />
                      </div>
                      <div>
                        <h3 className="font-outfit font-semibold text-lg">No hay videos procesados</h3>
                        <p className="text-muted-foreground text-sm">
                          Usa el panel de la derecha para convertir tu video
                        </p>
                      </div>
                    </CardContent>
                  </Card>
                ) : (
                  <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-5">
                    

{renditions.map((rendition) => {
  const platform =
    platformBadgeStyles[rendition.platform] ?? {
      label: rendition.platform,
      className: "bg-muted text-white"
    };

  const modeLabel = processingModeLabels[rendition.processingMode] ?? rendition.processingMode;
  const qualityLabel = qualityLabels[rendition.quality] ?? rendition.quality;
  const bgLabel = backgroundLabels[rendition.backgroundMode] ?? rendition.backgroundMode;

  const PlatformIcon =
    rendition.platform === "tiktok"
      ? TikTokIcon
      : rendition.platform === "instagram"
      ? InstagramIcon
      : YouTubeIcon;

  return (
    <Card
      key={rendition.id}
      className="overflow-hidden border-border/50 group hover:shadow-xl transition-all duration-300 hover:-translate-y-1 flex flex-col"
    >
      {/* MEDIA */}
      <div className="relative aspect-[9/16] bg-black overflow-hidden">

        {rendition.thumbnailUrl && (
          <img
            src={rendition.thumbnailUrl}
            alt="thumbnail"
            className="absolute inset-0 w-full h-full object-cover transition-opacity duration-300 group-hover:opacity-0"
          />
        )}

        {rendition.previewUrl && (
          <video
            src={rendition.previewUrl}
            muted
            loop
            playsInline
            autoPlay
            className="absolute inset-0 w-full h-full object-cover opacity-0 group-hover:opacity-100 transition-opacity duration-300"
          />
        )}

        {/* platform icon */}
        <div className="absolute top-2 left-2 bg-black/60 backdrop-blur rounded-full p-1.5">
          <PlatformIcon className="h-4 w-4 text-white"/>
        </div>

        {/* delete */}
        <Button
          size="icon"
          variant="destructive"
          className="absolute top-2 right-2 h-8 w-8 opacity-0 group-hover:opacity-100 transition"
          onClick={() => {
            setSelectedRendition(rendition);
            setIsDeleteRenditionOpen(true);
          }}
        >
          <Trash2 className="h-4 w-4" />
        </Button>

        {/* open preview */}
        <button
          className="absolute inset-0 flex items-center justify-center bg-black/30 opacity-0 group-hover:opacity-100 transition"
          onClick={() => {
            setPreviewVideo(video);
            setPreviewRendition(rendition);
            setIsPreviewOpen(true);
          }}
        >
          <div className="w-12 h-12 rounded-full bg-white flex items-center justify-center shadow">
            <Play className="h-5 w-5 text-black ml-0.5" />
          </div>
        </button>
      </div>

      {/* INFO */}
      <CardContent className="p-3 flex flex-col gap-2 flex-1">

        <div className="text-sm font-semibold leading-tight">
          {modeLabel}
        </div>

        {rendition.segmentDuration && (
          <div className="text-xs text-muted-foreground">
            {formatDuration(rendition.segmentStart)} → {formatDuration(rendition.segmentStart + rendition.segmentDuration)}
          </div>
        )}

        <div className="flex flex-wrap gap-1 text-[11px]">
          <Badge variant="outline">{qualityLabel}</Badge>
          <Badge variant="outline">{bgLabel}</Badge>
        </div>

        {rendition.createdAt && (
          <div className="text-[11px] text-muted-foreground">
            {timeAgo(rendition.createdAt)}
          </div>
        )}

        {/* ACTIONS */}
        <div className="flex gap-2 mt-auto">

          <Button
            size="sm"
            className="flex-1"
            onClick={() => {
              setPreviewVideo(video);
              setPreviewRendition(rendition);
              setIsPreviewOpen(true);
            }}
          >
            <Eye className="h-3 w-3 mr-1" />
            Ver
          </Button>

          {rendition.outputUrl && (
            <Button asChild size="sm" variant="secondary">
              <a href={rendition.outputUrl} download>
                <Download className="h-3 w-3" />
              </a>
            </Button>
          )}
        </div>
      </CardContent>
    </Card>
  );
})}

                  </div>
                )}
              </TabsContent>

              {/* Jobs Tab */}
              <TabsContent value="jobs" className="space-y-4">
                {jobsLoading ? (
                  <div className="space-y-4">
                    {[...Array(3)].map((_, i) => (
                      <Skeleton key={i} className="h-20 rounded-xl" />
                    ))}
                  </div>
                ) : jobs.length === 0 ? (
                  <Card className="text-center py-12 border-dashed border-2">
                    <CardContent className="space-y-4">
                      <div className="w-16 h-16 mx-auto rounded-full bg-blue-500/10 flex items-center justify-center">
                        <Clock className="h-8 w-8 text-blue-500" />
                      </div>
                      <div>
                        <h3 className="font-outfit font-semibold text-lg">No hay jobs</h3>
                        <p className="text-muted-foreground text-sm">
                          Los jobs aparecerán aquí cuando proceses un video
                        </p>
                      </div>
                    </CardContent>
                  </Card>
                ) : (
                  <div className="space-y-3">
                    {jobs.map((job) => {
                      const status = jobStatusConfig[job.status?.toLowerCase()] || jobStatusConfig.pending;
                      const StatusIcon = status.icon;
                      
                      return (
                        <Card key={job.id || job.jobId} className="border-border/50" data-testid={`job-${job.id || job.jobId}`}>
                          <CardContent className="p-4">
                            <div className="flex items-center justify-between">
                              <div className="flex items-center gap-4">
                                <div className={`p-2 rounded-lg ${status.className}`}>
                                  <StatusIcon className={`h-5 w-5 ${job.status === 'processing' ? 'animate-spin' : ''}`} />
                                </div>
                                <div>
                                  <div className="flex items-center gap-2">
                                    <Badge className={`${status.className} border font-medium`}>
                                      {status.label}
                                    </Badge>
                                    <span className="text-sm font-medium">
                                      {job.processingMode}
                                    </span>
                                  </div>
                                  <p className="text-xs text-muted-foreground mt-1">
                                    ID: {(job.id || job.jobId).slice(0, 8)}...
                                  </p>
                                </div>
                              </div>
                              {(job.status === 'pending' || job.status === 'processing') && (
                                <Button
                                  variant="outline"
                                  size="sm"
                                  onClick={() => cancelJobMutation.mutate(job.id || job.jobId)}
                                  disabled={cancelJobMutation.isPending}
                                  className="text-destructive hover:text-destructive"
                                >
                                  <XCircle className="mr-2 h-4 w-4" />
                                  Cancelar
                                </Button>
                              )}
                            </div>
                            {job.status === 'processing' && (
                              <Progress value={job.progress || 50} className="mt-4 h-2" />
                            )}
                          </CardContent>
                        </Card>
                      );
                    })}
                  </div>
                )}
              </TabsContent>
            </Tabs>
          </div>

          {/* Processing Panel - Clean Design */}
          <div className="space-y-6">
            <Card className="border-border/50 bg-card dark:bg-card/95 shadow-xl sticky top-24 overflow-hidden">
              <CardHeader className="pb-4">
                <div className="flex items-center gap-3">
                  <div className="p-2.5 rounded-xl bg-gradient-to-br from-indigo-500 to-purple-600 shadow-lg shadow-purple-500/20">
                    <Wand2 className="h-5 w-5 text-white" />
                  </div>
                  <div>
                    <CardTitle className="font-outfit text-lg">
                      Procesar video
                    </CardTitle>
                    <CardDescription className="text-xs">
                      Convierte a formato vertical 9:16
                    </CardDescription>
                  </div>
                </div>
              </CardHeader>
              
              <CardContent className="space-y-5">
                {/* Processing Mode */}
                <div className="space-y-2.5">
                  <Label className="text-sm font-medium">Modo de conversión</Label>
                  <div className="grid gap-2">
                    {[
                      { value: 'vertical', label: 'Video completo', desc: 'Convierte todo el video', icon: '📹' },
                      { value: 'short_auto', label: 'Short automático', desc: 'IA selecciona lo mejor', icon: '✨' },
                      { value: 'short_manual', label: 'Short manual', desc: 'Tú eliges el momento', icon: '✂️' },
                    ].map((mode) => (
                      <button
                        key={mode.value}
                        type="button"
                        onClick={() => setProcessingMode(mode.value)}
                        className={`w-full p-3 rounded-xl text-left transition-all flex items-center gap-3 ${
                          processingMode === mode.value
                            ? 'bg-indigo-500/10 border-2 border-indigo-500/50 dark:bg-indigo-500/20'
                            : 'bg-muted/50 border-2 border-transparent hover:bg-muted hover:border-border'
                        }`}
                      >
                        <span className="text-xl">{mode.icon}</span>
                        <div>
                          <p className={`font-medium text-sm ${processingMode === mode.value ? 'text-indigo-600 dark:text-indigo-400' : ''}`}>
                            {mode.label}
                          </p>
                          <p className="text-xs text-muted-foreground">{mode.desc}</p>
                        </div>
                      </button>
                    ))}
                  </div>
                </div>

                {/* Platform Selection with Icons */}
                <div className="space-y-2.5">
                  <Label className="text-sm font-medium">Plataforma</Label>
                  <div className="grid grid-cols-3 gap-2">
                    {[
                      { value: 'tiktok', label: 'TikTok', icon: TikTokIcon, color: 'from-gray-900 to-gray-800', activeColor: 'from-[#ff0050] to-[#00f2ea]' },
                      { value: 'instagram', label: 'Reels', icon: InstagramIcon, color: 'from-gray-900 to-gray-800', activeColor: 'from-[#833ab4] via-[#fd1d1d] to-[#fcb045]' },
                      { value: 'youtube_shorts', label: 'Shorts', icon: YouTubeIcon, color: 'from-gray-900 to-gray-800', activeColor: 'from-[#ff0000] to-[#cc0000]' },
                    ].map((p) => (
                      <button
                        key={p.value}
                        type="button"
                        onClick={() => setPlatform(p.value)}
                        className={`p-3 rounded-xl text-center transition-all flex flex-col items-center gap-1.5 ${
                          platform === p.value
                            ? `bg-gradient-to-br ${p.activeColor} shadow-lg text-white`
                            : 'bg-muted/50 border border-border hover:bg-muted text-muted-foreground hover:text-foreground'
                        }`}
                      >
                        <p.icon className="h-5 w-5" />
                        <p className="font-medium text-xs">{p.label}</p>
                      </button>
                    ))}
                  </div>
                </div>

                {/* Quality & Background */}
                <div className="grid grid-cols-2 gap-3">
                  <div className="space-y-2">
                    <Label className="text-xs font-medium">Calidad</Label>
                    <Select value={quality} onValueChange={setQuality}>
                      <SelectTrigger className="h-9 text-sm" data-testid="quality-select">
                        <SelectValue />
                      </SelectTrigger>
                      <SelectContent>
                        {qualityOptions.map((opt) => (
                          <SelectItem key={opt.value} value={opt.value}>
                            {opt.label}
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                  </div>
                  <div className="space-y-2">
                    <Label className="text-xs font-medium">Fondo</Label>
                    <Select value={backgroundMode} onValueChange={setBackgroundMode}>
                      <SelectTrigger className="h-9 text-sm" data-testid="background-mode-select">
                        <SelectValue />
                      </SelectTrigger>
                      <SelectContent>
                        {backgroundModeOptions.map((opt) => (
                          <SelectItem key={opt.value} value={opt.value}>
                            {opt.label}
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                  </div>
                </div>

                {/* Short Auto Duration */}
                {processingMode === 'short_auto' && (
                  <div className="space-y-3 p-4 rounded-xl bg-amber-500/10 border border-amber-500/20">
                    <div className="flex justify-between items-center">
                      <Label className="text-sm">Duración del short</Label>
                      <span className="px-2.5 py-0.5 rounded-full bg-amber-500/20 text-amber-600 dark:text-amber-400 text-sm font-bold">
                        {shortAutoDuration}s
                      </span>
                    </div>
                    <Slider
                      value={[shortAutoDuration]}
                      onValueChange={([v]) => setShortAutoDuration(v)}
                      min={5}
                      max={60}
                      step={5}
                      className="py-1"
                      data-testid="short-duration-slider"
                    />
                    <div className="flex justify-between text-[10px] text-muted-foreground">
                      <span>5s</span>
                      <span>60s</span>
                    </div>
                  </div>
                )}

                {/* Short Manual Options */}
                {processingMode === 'short_manual' && (
                  <div className="space-y-3 p-4 rounded-xl bg-sky-500/10 border border-sky-500/20">
                    <div className="flex items-center gap-2 text-sky-600 dark:text-sky-400">
                      <Scissors className="h-4 w-4" />
                      <span className="text-sm font-medium">Configurar corte</span>
                    </div>
                    <div className="grid grid-cols-2 gap-3">
                      <div className="space-y-1.5">
                        <Label className="text-xs text-muted-foreground">Inicio (seg)</Label>
                        <Input
                          type="number"
                          value={shortStartTime}
                          onChange={(e) => setShortStartTime(Number(e.target.value))}
                          min={0}
                          className="h-9 text-center"
                          data-testid="short-start-time-input"
                        />
                      </div>
                      <div className="space-y-1.5">
                        <Label className="text-xs text-muted-foreground">Duración</Label>
                        <div className="h-9 px-3 rounded-md bg-muted flex items-center justify-center">
                          <span className="font-medium text-sm">{shortDuration}s</span>
                        </div>
                      </div>
                    </div>
                    <Slider
                      value={[shortDuration]}
                      onValueChange={([v]) => setShortDuration(v)}
                      min={5}
                      max={60}
                      step={5}
                      data-testid="short-manual-duration-slider"
                    />
                    <div className="text-center text-xs text-muted-foreground">
                      Resultado: {formatDuration(shortStartTime)} → {formatDuration(shortStartTime + shortDuration)}
                    </div>
                  </div>
                )}

                {/* Advanced Options Toggle */}
                <div 
                  className="flex items-center justify-between p-3 rounded-xl bg-muted/50 cursor-pointer hover:bg-muted transition-all"
                  onClick={() => setShowAdvanced(!showAdvanced)}
                >
                  <div className="flex items-center gap-2">
                    <Settings2 className="h-4 w-4 text-muted-foreground" />
                    <Label className="cursor-pointer text-sm">Opciones avanzadas</Label>
                  </div>
                  <Switch
                    checked={showAdvanced}
                    onCheckedChange={setShowAdvanced}
                    data-testid="advanced-options-toggle"
                  />
                </div>

                {/* Advanced Options */}
                {showAdvanced && (
                  <div className="space-y-4 p-4 rounded-xl bg-muted/30 border border-dashed border-border">
                    <div className="space-y-2">
                      <div className="flex justify-between items-center">
                        <Label className="text-xs">Espacio superior (headroom)</Label>
                        <span className="text-xs text-muted-foreground bg-muted px-2 py-0.5 rounded">
                          {(headroomRatio * 100).toFixed(0)}%
                        </span>
                      </div>
                      <Slider
                        value={[headroomRatio]}
                        onValueChange={([v]) => setHeadroomRatio(v)}
                        min={0}
                        max={0.3}
                        step={0.05}
                      />
                    </div>
                    <div className="space-y-2">
                      <div className="flex justify-between items-center">
                        <Label className="text-xs">Suavizado de cámara</Label>
                        <span className="text-xs text-muted-foreground bg-muted px-2 py-0.5 rounded">
                          {(smoothingStrength * 100).toFixed(0)}%
                        </span>
                      </div>
                      <Slider
                        value={[smoothingStrength]}
                        onValueChange={([v]) => setSmoothingStrength(v)}
                        min={0}
                        max={1}
                        step={0.1}
                      />
                    </div>
                  </div>
                )}

                {/* Process Button */}
                <Button
                  className="w-full h-12 bg-gradient-to-r from-indigo-600 to-purple-600 hover:from-indigo-700 hover:to-purple-700 shadow-lg shadow-indigo-500/25 hover:shadow-indigo-500/40 transition-all text-base font-semibold rounded-xl"
                  onClick={handleProcess}
                  disabled={processMutation.isPending}
                  data-testid="process-video-button"
                >
                  {processMutation.isPending ? (
                    <Loader2 className="mr-2 h-5 w-5 animate-spin" />
                  ) : (
                    <Sparkles className="mr-2 h-5 w-5" />
                  )}
                  Convertir a vertical
                </Button>

                <p className="text-center text-[10px] text-muted-foreground">
                  El procesamiento puede tardar unos minutos
                </p>
              </CardContent>
            </Card>
          </div>
        </div>

        {/* Video Preview Modal */}
        <VideoPreviewModal
          isOpen={isPreviewOpen}
          onClose={() => {
            setIsPreviewOpen(false);
            setPreviewRendition(null);
          }}
          video={previewVideo}
          rendition={previewRendition}
        />

        {/* Delete Rendition Alert */}
        <AlertDialog open={isDeleteRenditionOpen} onOpenChange={setIsDeleteRenditionOpen}>
          <AlertDialogContent>
            <AlertDialogHeader>
              <AlertDialogTitle>¿Eliminar video procesado?</AlertDialogTitle>
              <AlertDialogDescription>
                Esta acción no se puede deshacer.
              </AlertDialogDescription>
            </AlertDialogHeader>
            <AlertDialogFooter>
              <AlertDialogCancel>Cancelar</AlertDialogCancel>
              <AlertDialogAction
                onClick={() => deleteRenditionMutation.mutate(selectedRendition?.id)}
                className="bg-destructive hover:bg-destructive/90"
              >
                {deleteRenditionMutation.isPending && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
                Eliminar
              </AlertDialogAction>
            </AlertDialogFooter>
          </AlertDialogContent>
        </AlertDialog>
      </div>
    </Layout>
  );
}
