import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { useMutation } from '@tanstack/react-query';
import { useAuth } from '@/context/AuthContext';
import { usersApi } from '@/api/users';
import { Layout } from '@/components/Layout';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from '@/components/ui/card';
import { Separator } from '@/components/ui/separator';
import { toast } from 'sonner';
import { User, Lock, Loader2, Eye, EyeOff } from 'lucide-react';

const profileSchema = z.object({
  firstName: z.string().min(1, 'El nombre es requerido'),
  lastName: z.string().min(1, 'El apellido es requerido'),
});

const passwordRegex = /^(?=.*[A-ZÑ])(?=.*[a-zñ])(?=.*\d)(?=.*[-@#$%^&*.,()_+{}|;:'"<>/!¡¿?])[A-ZÑa-zñ\d-@#$%^&*.,()_+{}|;:'"<>/!¡¿?]{8,}$/;

const passwordSchema = z.object({
  currentPassword: z.string().min(1, 'La contraseña actual es requerida'),
  newPassword: z.string()
    .min(8, 'La contraseña debe tener al menos 8 caracteres')
    .regex(passwordRegex, 'La contraseña debe tener mayúscula, minúscula, número y carácter especial'),
  confirmPassword: z.string(),
}).refine((data) => data.newPassword === data.confirmPassword, {
  message: 'Las contraseñas no coinciden',
  path: ['confirmPassword'],
});

export function ProfilePage() {
  const { user, updateUser } = useAuth();
  const [showPassword, setShowPassword] = useState(false);

  const profileForm = useForm({
    resolver: zodResolver(profileSchema),
    defaultValues: {
      firstName: user?.firstName || '',
      lastName: user?.lastName || '',
    },
  });

  const passwordForm = useForm({
    resolver: zodResolver(passwordSchema),
    defaultValues: {
      currentPassword: '',
      newPassword: '',
      confirmPassword: '',
    },
  });

  const updateProfileMutation = useMutation({
    mutationFn: usersApi.update,
    onSuccess: (response) => {
      const updatedUser = response.data || response;
      updateUser({ ...user, ...updatedUser });
      toast.success('Perfil actualizado');
    },
    onError: (error) => {
      toast.error(error.response?.data?.message || 'Error al actualizar perfil');
    },
  });

  const changePasswordMutation = useMutation({
    mutationFn: (data) => usersApi.changePassword(data),
    onSuccess: () => {
      toast.success('Contraseña actualizada');
      passwordForm.reset();
    },
    onError: (error) => {
      toast.error(error.response?.data?.message || 'Error al cambiar contraseña');
    },
  });

  const onProfileSubmit = (data) => {
    updateProfileMutation.mutate(data);
  };

  const onPasswordSubmit = (data) => {
    changePasswordMutation.mutate({
      currentPassword: data.currentPassword,
      newPassword: data.newPassword,
    });
  };

  return (
    <Layout>
      <div className="max-w-2xl mx-auto space-y-8" data-testid="profile-page">
        <div>
          <h1 className="font-outfit text-3xl font-semibold tracking-tight">Perfil</h1>
          <p className="text-muted-foreground mt-1">Administra tu cuenta</p>
        </div>

        {/* Profile Info */}
        <Card>
          <form onSubmit={profileForm.handleSubmit(onProfileSubmit)}>
            <CardHeader>
              <CardTitle className="font-outfit flex items-center gap-2">
                <User className="h-5 w-5 text-accent" />
                Información personal
              </CardTitle>
              <CardDescription>Actualiza tu nombre y apellido</CardDescription>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="space-y-2">
                <Label htmlFor="email">Email</Label>
                <Input
                  id="email"
                  value={user?.email || ''}
                  disabled
                  className="bg-muted"
                />
              </div>
              <div className="grid grid-cols-2 gap-4">
                <div className="space-y-2">
                  <Label htmlFor="firstName">Nombre</Label>
                  <Input
                    id="firstName"
                    data-testid="profile-firstname-input"
                    {...profileForm.register('firstName')}
                  />
                  {profileForm.formState.errors.firstName && (
                    <p className="text-sm text-destructive">
                      {profileForm.formState.errors.firstName.message}
                    </p>
                  )}
                </div>
                <div className="space-y-2">
                  <Label htmlFor="lastName">Apellido</Label>
                  <Input
                    id="lastName"
                    data-testid="profile-lastname-input"
                    {...profileForm.register('lastName')}
                  />
                  {profileForm.formState.errors.lastName && (
                    <p className="text-sm text-destructive">
                      {profileForm.formState.errors.lastName.message}
                    </p>
                  )}
                </div>
              </div>
            </CardContent>
            <CardFooter>
              <Button
                type="submit"
                className="bg-accent hover:bg-accent/90"
                disabled={updateProfileMutation.isPending}
                data-testid="update-profile-button"
              >
                {updateProfileMutation.isPending && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
                Guardar cambios
              </Button>
            </CardFooter>
          </form>
        </Card>

        {/* Change Password */}
        <Card>
          <form onSubmit={passwordForm.handleSubmit(onPasswordSubmit)}>
            <CardHeader>
              <CardTitle className="font-outfit flex items-center gap-2">
                <Lock className="h-5 w-5 text-accent" />
                Cambiar contraseña
              </CardTitle>
              <CardDescription>Actualiza tu contraseña de acceso</CardDescription>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="space-y-2">
                <Label htmlFor="currentPassword">Contraseña actual</Label>
                <div className="relative">
                  <Input
                    id="currentPassword"
                    type={showPassword ? 'text' : 'password'}
                    data-testid="current-password-input"
                    {...passwordForm.register('currentPassword')}
                  />
                  <Button
                    type="button"
                    variant="ghost"
                    size="icon"
                    className="absolute right-0 top-0 h-full px-3"
                    onClick={() => setShowPassword(!showPassword)}
                  >
                    {showPassword ? <EyeOff className="h-4 w-4" /> : <Eye className="h-4 w-4" />}
                  </Button>
                </div>
                {passwordForm.formState.errors.currentPassword && (
                  <p className="text-sm text-destructive">
                    {passwordForm.formState.errors.currentPassword.message}
                  </p>
                )}
              </div>
              <div className="space-y-2">
                <Label htmlFor="newPassword">Nueva contraseña</Label>
                <Input
                  id="newPassword"
                  type={showPassword ? 'text' : 'password'}
                  data-testid="new-password-input"
                  {...passwordForm.register('newPassword')}
                />
                {passwordForm.formState.errors.newPassword && (
                  <p className="text-sm text-destructive">
                    {passwordForm.formState.errors.newPassword.message}
                  </p>
                )}
              </div>
              <div className="space-y-2">
                <Label htmlFor="confirmPassword">Confirmar nueva contraseña</Label>
                <Input
                  id="confirmPassword"
                  type={showPassword ? 'text' : 'password'}
                  data-testid="confirm-new-password-input"
                  {...passwordForm.register('confirmPassword')}
                />
                {passwordForm.formState.errors.confirmPassword && (
                  <p className="text-sm text-destructive">
                    {passwordForm.formState.errors.confirmPassword.message}
                  </p>
                )}
              </div>
            </CardContent>
            <CardFooter>
              <Button
                type="submit"
                className="bg-accent hover:bg-accent/90"
                disabled={changePasswordMutation.isPending}
                data-testid="change-password-button"
              >
                {changePasswordMutation.isPending && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
                Cambiar contraseña
              </Button>
            </CardFooter>
          </form>
        </Card>
      </div>
    </Layout>
  );
}
