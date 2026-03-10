import { useState } from 'react';
import { Link } from 'react-router-dom';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { projectsApi } from '@/api/projects';
import { Layout } from '@/components/Layout';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Textarea } from '@/components/ui/textarea';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle, DialogTrigger } from '@/components/ui/dialog';
import { Skeleton } from '@/components/ui/skeleton';
import { toast } from 'sonner';
import { Plus, Folder, Film, MoreVertical, Pencil, Trash2, Loader2, Sparkles, ArrowRight, FolderOpen } from 'lucide-react';
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

export function DashboardPage() {
  const [isCreateOpen, setIsCreateOpen] = useState(false);
  const [isEditOpen, setIsEditOpen] = useState(false);
  const [isDeleteOpen, setIsDeleteOpen] = useState(false);
  const [selectedProject, setSelectedProject] = useState(null);
  const [projectName, setProjectName] = useState('');
  const [projectDescription, setProjectDescription] = useState('');
  const queryClient = useQueryClient();

  const { data: projectsData, isLoading, error } = useQuery({
    queryKey: ['projects'],
    queryFn: () => projectsApi.getAll({ page: 0, size: 50 }),
  });

  const createMutation = useMutation({
    mutationFn: projectsApi.create,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['projects'] });
      setIsCreateOpen(false);
      setProjectName('');
      setProjectDescription('');
      toast.success('¡Proyecto creado exitosamente!');
    },
    onError: (error) => {
      toast.error(error.response?.data?.message || 'Error al crear proyecto');
    },
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, data }) => projectsApi.update(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['projects'] });
      setIsEditOpen(false);
      setSelectedProject(null);
      toast.success('Proyecto actualizado');
    },
    onError: (error) => {
      toast.error(error.response?.data?.message || 'Error al actualizar');
    },
  });

  const deleteMutation = useMutation({
    mutationFn: projectsApi.delete,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['projects'] });
      setIsDeleteOpen(false);
      setSelectedProject(null);
      toast.success('Proyecto eliminado');
    },
    onError: (error) => {
      toast.error(error.response?.data?.message || 'Error al eliminar');
    },
  });

  const handleCreate = (e) => {
    e.preventDefault();
    createMutation.mutate({ name: projectName, description: projectDescription });
  };

  const handleEdit = (e) => {
    e.preventDefault();
    updateMutation.mutate({
      id: selectedProject.id,
      data: { name: projectName, description: projectDescription },
    });
  };

  const openEditDialog = (project) => {
    setSelectedProject(project);
    setProjectName(project.name);
    setProjectDescription(project.description || '');
    setIsEditOpen(true);
  };

  const openDeleteDialog = (project) => {
    setSelectedProject(project);
    setIsDeleteOpen(true);
  };

  const projects = projectsData?.data?.content || projectsData?.content || [];

  return (
    <Layout>
      <div className="space-y-8" data-testid="dashboard-page">
        {/* Header */}
        <div className="flex flex-col sm:flex-row sm:items-end sm:justify-between gap-6">
          <div className="space-y-1">
            <h1 className="font-outfit text-4xl font-bold tracking-tight">
              Mis <span className="gradient-text">Proyectos</span>
            </h1>
            <p className="text-muted-foreground text-lg">
              Organiza y convierte tus videos a formato vertical
            </p>
          </div>
          <Dialog open={isCreateOpen} onOpenChange={setIsCreateOpen}>
            <DialogTrigger asChild>
              <Button 
                size="lg"
                className="bg-gradient-to-r from-blue-500 to-purple-600 hover:from-blue-600 hover:to-purple-700 shadow-lg shadow-purple-500/25 hover:shadow-purple-500/40 transition-all"
                data-testid="create-project-button"
              >
                <Plus className="mr-2 h-5 w-5" />
                Nuevo proyecto
              </Button>
            </DialogTrigger>
            <DialogContent className="sm:max-w-md">
              <form onSubmit={handleCreate}>
                <DialogHeader>
                  <DialogTitle className="font-outfit text-xl">Crear proyecto</DialogTitle>
                  <DialogDescription>
                    Los proyectos te ayudan a organizar tus videos
                  </DialogDescription>
                </DialogHeader>
                <div className="space-y-4 py-6">
                  <div className="space-y-2">
                    <Label htmlFor="name">Nombre del proyecto</Label>
                    <Input
                      id="name"
                      value={projectName}
                      onChange={(e) => setProjectName(e.target.value)}
                      placeholder="Ej: Videos de TikTok"
                      className="h-11"
                      data-testid="project-name-input"
                      required
                    />
                  </div>
                  <div className="space-y-2">
                    <Label htmlFor="description">Descripción (opcional)</Label>
                    <Textarea
                      id="description"
                      value={projectDescription}
                      onChange={(e) => setProjectDescription(e.target.value)}
                      placeholder="Describe tu proyecto..."
                      className="resize-none"
                      rows={3}
                      data-testid="project-description-input"
                    />
                  </div>
                </div>
                <DialogFooter>
                  <Button 
                    type="submit" 
                    className="w-full bg-gradient-to-r from-blue-500 to-purple-600 hover:from-blue-600 hover:to-purple-700" 
                    disabled={createMutation.isPending} 
                    data-testid="create-project-submit"
                  >
                    {createMutation.isPending && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
                    Crear proyecto
                  </Button>
                </DialogFooter>
              </form>
            </DialogContent>
          </Dialog>
        </div>

        {/* Stats */}
        {projects.length > 0 && (
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-4">
            <div className="stat-card rounded-xl p-5">
              <p className="text-3xl font-bold font-outfit text-foreground">{projects.length}</p>
              <p className="text-sm text-muted-foreground mt-1">Proyectos</p>
            </div>
            <div className="stat-card rounded-xl p-5">
              <p className="text-3xl font-bold font-outfit text-foreground">
                {projects.reduce((acc, p) => acc + (p.videoCount || 0), 0)}
              </p>
              <p className="text-sm text-muted-foreground mt-1">Videos totales</p>
            </div>
            <div className="stat-card rounded-xl p-5">
              <p className="text-3xl font-bold font-outfit gradient-text">∞</p>
              <p className="text-sm text-muted-foreground mt-1">Conversiones</p>
            </div>
            <div className="stat-card rounded-xl p-5">
              <p className="text-3xl font-bold font-outfit text-emerald-500">Activo</p>
              <p className="text-sm text-muted-foreground mt-1">Estado</p>
            </div>
          </div>
        )}

        {/* Projects Grid */}
        {isLoading ? (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {[...Array(6)].map((_, i) => (
              <Card key={i} className="overflow-hidden">
                <div className="h-32 bg-gradient-to-br from-muted to-muted/50" />
                <CardHeader>
                  <Skeleton className="h-5 w-3/4" />
                  <Skeleton className="h-4 w-1/2 mt-2" />
                </CardHeader>
              </Card>
            ))}
          </div>
        ) : error ? (
          <Card className="text-center py-16 border-destructive/50">
            <CardContent className="space-y-4">
              <p className="text-destructive">Error al cargar proyectos</p>
              <Button onClick={() => queryClient.invalidateQueries({ queryKey: ['projects'] })}>
                Reintentar
              </Button>
            </CardContent>
          </Card>
        ) : projects.length === 0 ? (
          <div className="relative">
            <div className="absolute inset-0 empty-state-bg rounded-3xl" />
            <Card className="relative text-center py-20 border-dashed border-2 bg-transparent">
              <CardContent className="space-y-6">
                <div className="relative inline-flex">
                  <div className="absolute inset-0 bg-gradient-to-r from-blue-500 to-purple-500 rounded-full blur-xl opacity-30 animate-pulse" />
                  <div className="relative p-6 rounded-full bg-gradient-to-br from-blue-500/10 to-purple-500/10 border border-purple-500/20">
                    <FolderOpen className="h-16 w-16 text-purple-500" />
                  </div>
                </div>
                <div className="space-y-2">
                  <h3 className="font-outfit text-2xl font-semibold">
                    Crea tu primer proyecto
                  </h3>
                  <p className="text-muted-foreground max-w-sm mx-auto">
                    Los proyectos te ayudan a organizar tus videos y generar contenido vertical para redes sociales
                  </p>
                </div>
                <Button 
                  size="lg"
                  className="bg-gradient-to-r from-blue-500 to-purple-600 hover:from-blue-600 hover:to-purple-700 shadow-lg"
                  onClick={() => setIsCreateOpen(true)}
                >
                  <Sparkles className="mr-2 h-5 w-5" />
                  Crear mi primer proyecto
                </Button>
              </CardContent>
            </Card>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 relative">
            {projects.map((project, index) => (
              <Link 
                key={project.id} 
                to={`/projects/${project.id}`}
                className="group block"
              >
                <Card 
                  className="card-3d overflow-hidden border-border/50 bg-card hover:border-indigo-500/40 dark:bg-card/80"
                  data-testid={`project-card-${project.id}`}
                >
                  {/* Gradient header */}
                  <div 
                    className="h-24 relative overflow-hidden"
                    style={{
                      background: `linear-gradient(135deg, 
                        hsl(${220 + (index * 30) % 60}, 70%, ${50 + (index % 3) * 5}%), 
                        hsl(${260 + (index * 30) % 60}, 70%, ${45 + (index % 3) * 5}%))`
                    }}
                  >
                    <div className="absolute inset-0 bg-black/10" />
                    <div className="absolute bottom-0 left-0 right-0 h-20 bg-gradient-to-t from-card dark:from-card/95 to-transparent" />
                    <div className="absolute top-3 right-3 z-10">
                      <DropdownMenu>
                        <DropdownMenuTrigger asChild onClick={(e) => e.preventDefault()}>
                          <Button 
                            variant="ghost" 
                            size="icon" 
                            className="h-8 w-8 bg-black/30 hover:bg-black/50 text-white"
                            data-testid={`project-menu-${project.id}`}
                          >
                            <MoreVertical className="h-4 w-4" />
                          </Button>
                        </DropdownMenuTrigger>
                        <DropdownMenuContent align="end">
                          <DropdownMenuItem onClick={(e) => { e.preventDefault(); openEditDialog(project); }}>
                            <Pencil className="mr-2 h-4 w-4" />
                            Editar
                          </DropdownMenuItem>
                          <DropdownMenuItem 
                            onClick={(e) => { e.preventDefault(); openDeleteDialog(project); }} 
                            className="text-destructive focus:text-destructive"
                          >
                            <Trash2 className="mr-2 h-4 w-4" />
                            Eliminar
                          </DropdownMenuItem>
                        </DropdownMenuContent>
                      </DropdownMenu>
                    </div>
                    <div className="absolute -bottom-5 left-4">
                      <div className="p-3 rounded-xl bg-card dark:bg-slate-800 border border-border/50 shadow-lg">
                        <Folder className="h-6 w-6 text-indigo-500 group-hover:text-purple-500 transition-colors" />
                      </div>
                    </div>
                  </div>
                  
                  <CardHeader className="pt-8">
                    <CardTitle className="font-outfit text-lg text-foreground group-hover:text-indigo-500 transition-colors flex items-center justify-between">
                      <span className="truncate">{project.name}</span>
                      <ArrowRight className="h-4 w-4 opacity-0 -translate-x-2 group-hover:opacity-100 group-hover:translate-x-0 transition-all flex-shrink-0" />
                    </CardTitle>
                    {project.description && (
                      <CardDescription className="line-clamp-2">
                        {project.description}
                      </CardDescription>
                    )}
                  </CardHeader>
                  <CardContent className="pt-0">
                    <div className="flex items-center gap-4 text-sm text-muted-foreground">
                      <div className="flex items-center gap-1.5">
                        <Film className="h-4 w-4" />
                        <span>{project.videoCount || 0} videos</span>
                      </div>
                    </div>
                  </CardContent>
                </Card>
              </Link>
            ))}
          </div>
        )}

        {/* Edit Dialog */}
        <Dialog open={isEditOpen} onOpenChange={setIsEditOpen}>
          <DialogContent>
            <form onSubmit={handleEdit}>
              <DialogHeader>
                <DialogTitle className="font-outfit">Editar proyecto</DialogTitle>
              </DialogHeader>
              <div className="space-y-4 py-4">
                <div className="space-y-2">
                  <Label htmlFor="edit-name">Nombre</Label>
                  <Input
                    id="edit-name"
                    value={projectName}
                    onChange={(e) => setProjectName(e.target.value)}
                    data-testid="edit-project-name-input"
                    required
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="edit-description">Descripción</Label>
                  <Textarea
                    id="edit-description"
                    value={projectDescription}
                    onChange={(e) => setProjectDescription(e.target.value)}
                    data-testid="edit-project-description-input"
                    rows={3}
                  />
                </div>
              </div>
              <DialogFooter>
                <Button type="submit" className="bg-gradient-to-r from-blue-500 to-purple-600" disabled={updateMutation.isPending}>
                  {updateMutation.isPending && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
                  Guardar cambios
                </Button>
              </DialogFooter>
            </form>
          </DialogContent>
        </Dialog>

        {/* Delete Alert */}
        <AlertDialog open={isDeleteOpen} onOpenChange={setIsDeleteOpen}>
          <AlertDialogContent>
            <AlertDialogHeader>
              <AlertDialogTitle>¿Eliminar proyecto?</AlertDialogTitle>
              <AlertDialogDescription>
                Esta acción no se puede deshacer. Se eliminarán todos los videos del proyecto.
              </AlertDialogDescription>
            </AlertDialogHeader>
            <AlertDialogFooter>
              <AlertDialogCancel>Cancelar</AlertDialogCancel>
              <AlertDialogAction
                onClick={() => deleteMutation.mutate(selectedProject?.id)}
                className="bg-destructive hover:bg-destructive/90"
                data-testid="confirm-delete-project"
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
