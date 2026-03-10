import { useState } from 'react';
import { Link, useNavigate, useSearchParams } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { authApi } from '@/api/auth';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from '@/components/ui/card';
import { toast } from 'sonner';
import { Film, Loader2, Eye, EyeOff, ArrowLeft, CheckCircle } from 'lucide-react';

const passwordRegex = /^(?=.*[A-ZÑ])(?=.*[a-zñ])(?=.*\d)(?=.*[-@#$%^&*.,()_+{}|;:'"<>/!¡¿?])[A-ZÑa-zñ\d-@#$%^&*.,()_+{}|;:'"<>/!¡¿?]{8,}$/;

const resetPasswordSchema = z.object({
  newPassword: z.string()
    .min(8, 'La contraseña debe tener al menos 8 caracteres')
    .regex(passwordRegex, 'La contraseña debe tener mayúscula, minúscula, número y carácter especial'),
  confirmPassword: z.string(),
}).refine((data) => data.newPassword === data.confirmPassword, {
  message: 'Las contraseñas no coinciden',
  path: ['confirmPassword'],
});

export function ResetPasswordPage() {
  const [isLoading, setIsLoading] = useState(false);
  const [showPassword, setShowPassword] = useState(false);
  const [isSuccess, setIsSuccess] = useState(false);
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const token = searchParams.get('token');

  const form = useForm({
    resolver: zodResolver(resetPasswordSchema),
    defaultValues: {
      newPassword: '',
      confirmPassword: '',
    },
  });

  const onSubmit = async (data) => {
    if (!token) {
      toast.error('Token inválido');
      return;
    }

    setIsLoading(true);
    try {
      await authApi.resetPassword({ token, newPassword: data.newPassword });
      setIsSuccess(true);
      toast.success('Contraseña actualizada');
    } catch (error) {
      const message = error.response?.data?.message || 'Error al restablecer la contraseña';
      toast.error(message);
    } finally {
      setIsLoading(false);
    }
  };

  if (!token) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-background p-4">
        <Card className="w-full max-w-md">
          <CardHeader className="text-center space-y-4">
            <CardTitle className="font-outfit text-2xl text-destructive">Token inválido</CardTitle>
            <CardDescription>
              El enlace de recuperación es inválido o ha expirado.
            </CardDescription>
          </CardHeader>
          <CardFooter>
            <Link to="/forgot-password" className="w-full">
              <Button className="w-full bg-accent hover:bg-accent/90">
                Solicitar nuevo enlace
              </Button>
            </Link>
          </CardFooter>
        </Card>
      </div>
    );
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-background p-4">
      <Card className="w-full max-w-md">
        <CardHeader className="text-center space-y-4">
          <div className="flex justify-center">
            <div className="p-3 rounded-xl bg-accent/10">
              {isSuccess ? <CheckCircle className="h-8 w-8 text-green-500" /> : <Film className="h-8 w-8 text-accent" />}
            </div>
          </div>
          <CardTitle className="font-outfit text-2xl">
            {isSuccess ? 'Contraseña actualizada' : 'Nueva contraseña'}
          </CardTitle>
          <CardDescription>
            {isSuccess
              ? 'Tu contraseña ha sido actualizada exitosamente'
              : 'Ingresa tu nueva contraseña'}
          </CardDescription>
        </CardHeader>
        {!isSuccess ? (
          <form onSubmit={form.handleSubmit(onSubmit)}>
            <CardContent className="space-y-4">
              <div className="space-y-2">
                <Label htmlFor="newPassword">Nueva contraseña</Label>
                <div className="relative">
                  <Input
                    id="newPassword"
                    type={showPassword ? 'text' : 'password'}
                    placeholder="********"
                    data-testid="reset-password-input"
                    {...form.register('newPassword')}
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
                {form.formState.errors.newPassword && (
                  <p className="text-sm text-destructive">{form.formState.errors.newPassword.message}</p>
                )}
              </div>
              <div className="space-y-2">
                <Label htmlFor="confirmPassword">Confirmar contraseña</Label>
                <Input
                  id="confirmPassword"
                  type={showPassword ? 'text' : 'password'}
                  placeholder="********"
                  data-testid="reset-confirm-password-input"
                  {...form.register('confirmPassword')}
                />
                {form.formState.errors.confirmPassword && (
                  <p className="text-sm text-destructive">{form.formState.errors.confirmPassword.message}</p>
                )}
              </div>
            </CardContent>
            <CardFooter className="flex flex-col gap-4">
              <Button
                type="submit"
                className="w-full bg-accent hover:bg-accent/90"
                disabled={isLoading}
                data-testid="reset-password-submit-button"
              >
                {isLoading && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
                Restablecer contraseña
              </Button>
            </CardFooter>
          </form>
        ) : (
          <CardFooter>
            <Button
              className="w-full bg-accent hover:bg-accent/90"
              onClick={() => navigate('/login')}
              data-testid="go-to-login-button"
            >
              Ir a iniciar sesión
            </Button>
          </CardFooter>
        )}
      </Card>
    </div>
  );
}
