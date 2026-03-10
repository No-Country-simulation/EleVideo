import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { authApi } from '@/api/auth';
import { useTheme } from '@/context/ThemeContext';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from '@/components/ui/card';
import { toast } from 'sonner';
import { Film, Loader2, Eye, EyeOff, Moon, Sun, Sparkles, CheckCircle } from 'lucide-react';

const passwordRegex = /^(?=.*[A-ZÑ])(?=.*[a-zñ])(?=.*\d)(?=.*[-@#$%^&*.,()_+{}|;:'"<>/!¡¿?])[A-ZÑa-zñ\d-@#$%^&*.,()_+{}|;:'"<>/!¡¿?]{8,}$/;

const registerSchema = z.object({
  firstName: z.string().min(1, 'El nombre es requerido'),
  lastName: z.string().min(1, 'El apellido es requerido'),
  email: z.string().email('Email inválido'),
  password: z.string()
    .min(8, 'La contraseña debe tener al menos 8 caracteres')
    .regex(passwordRegex, 'Debe tener mayúscula, minúscula, número y carácter especial'),
  confirmPassword: z.string(),
}).refine((data) => data.password === data.confirmPassword, {
  message: 'Las contraseñas no coinciden',
  path: ['confirmPassword'],
});

export function RegisterPage() {
  const [isLoading, setIsLoading] = useState(false);
  const [showPassword, setShowPassword] = useState(false);
  const { theme, toggleTheme } = useTheme();
  const navigate = useNavigate();

  const form = useForm({
    resolver: zodResolver(registerSchema),
    defaultValues: {
      firstName: '',
      lastName: '',
      email: '',
      password: '',
      confirmPassword: '',
    },
  });

  const password = form.watch('password');
  const passwordChecks = [
    { label: '8+ caracteres', valid: password?.length >= 8 },
    { label: 'Mayúscula', valid: /[A-ZÑ]/.test(password || '') },
    { label: 'Minúscula', valid: /[a-zñ]/.test(password || '') },
    { label: 'Número', valid: /\d/.test(password || '') },
    { label: 'Especial', valid: /[-@#$%^&*.,()_+{}|;:'"<>/!¡¿?]/.test(password || '') },
  ];

  const onSubmit = async (data) => {
    setIsLoading(true);
    try {
      const { confirmPassword, ...registerData } = data;
      await authApi.register(registerData);
      toast.success('¡Cuenta creada! Revisa tu email para verificarla.');
      navigate('/login');
    } catch (error) {
      const message = error.response?.data?.message || 'Error al registrarse';
      toast.error(message);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen auth-bg relative overflow-hidden">
      {/* Animated background elements */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 -left-20 w-72 h-72 bg-purple-500/20 rounded-full blur-3xl animate-float" />
        <div className="absolute bottom-1/4 -right-20 w-96 h-96 bg-blue-500/20 rounded-full blur-3xl animate-float" style={{ animationDelay: '1s' }} />
        <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-[600px] h-[600px] bg-gradient-to-r from-purple-500/10 to-blue-500/10 rounded-full blur-3xl" />
      </div>

      {/* Theme Toggle */}
      <Button
        variant="ghost"
        size="icon"
        className="absolute top-4 right-4 z-50 bg-background/50 backdrop-blur-sm"
        onClick={toggleTheme}
        data-testid="theme-toggle"
      >
        {theme === 'light' ? <Moon className="h-5 w-5" /> : <Sun className="h-5 w-5 text-yellow-400" />}
      </Button>

      {/* Content */}
      <div className="relative z-10 min-h-screen flex items-center justify-center p-4 py-12">
        <div className="w-full max-w-md space-y-6">
          {/* Logo */}
          <div className="text-center space-y-4">
            <div className="inline-flex items-center justify-center">
              <div className="relative">
                <div className="absolute inset-0 bg-gradient-to-r from-blue-500 to-purple-500 rounded-2xl blur-xl opacity-50" />
                <div className="relative p-4 rounded-2xl bg-gradient-to-br from-blue-500 to-purple-600 shadow-xl">
                  <Film className="h-10 w-10 text-white" />
                </div>
              </div>
            </div>
            <div>
              <h1 className="font-outfit text-3xl font-bold tracking-tight">
                Únete a <span className="gradient-text">Elevideo</span>
              </h1>
              <p className="text-muted-foreground mt-2">
                Crea tu cuenta gratis y empieza a crear contenido
              </p>
            </div>
          </div>

          {/* Card */}
          <Card className="border-border/50 bg-card/80 backdrop-blur-xl shadow-2xl">
            <form onSubmit={form.handleSubmit(onSubmit)}>
              <CardContent className="space-y-4 pt-6">
                <div className="grid grid-cols-2 gap-4">
                  <div className="space-y-2">
                    <Label htmlFor="firstName">Nombre</Label>
                    <Input
                      id="firstName"
                      placeholder="Juan"
                      className="h-11 bg-background/50 border-border/50"
                      data-testid="register-firstname-input"
                      {...form.register('firstName')}
                    />
                    {form.formState.errors.firstName && (
                      <p className="text-xs text-destructive">{form.formState.errors.firstName.message}</p>
                    )}
                  </div>
                  <div className="space-y-2">
                    <Label htmlFor="lastName">Apellido</Label>
                    <Input
                      id="lastName"
                      placeholder="Pérez"
                      className="h-11 bg-background/50 border-border/50"
                      data-testid="register-lastname-input"
                      {...form.register('lastName')}
                    />
                    {form.formState.errors.lastName && (
                      <p className="text-xs text-destructive">{form.formState.errors.lastName.message}</p>
                    )}
                  </div>
                </div>
                <div className="space-y-2">
                  <Label htmlFor="email">Email</Label>
                  <Input
                    id="email"
                    type="email"
                    placeholder="tu@email.com"
                    className="h-11 bg-background/50 border-border/50"
                    data-testid="register-email-input"
                    {...form.register('email')}
                  />
                  {form.formState.errors.email && (
                    <p className="text-xs text-destructive">{form.formState.errors.email.message}</p>
                  )}
                </div>
                <div className="space-y-2">
                  <Label htmlFor="password">Contraseña</Label>
                  <div className="relative">
                    <Input
                      id="password"
                      type={showPassword ? 'text' : 'password'}
                      placeholder="••••••••"
                      className="h-11 bg-background/50 border-border/50 pr-12"
                      data-testid="register-password-input"
                      {...form.register('password')}
                    />
                    <Button
                      type="button"
                      variant="ghost"
                      size="icon"
                      className="absolute right-0 top-0 h-full px-3 hover:bg-transparent"
                      onClick={() => setShowPassword(!showPassword)}
                    >
                      {showPassword ? <EyeOff className="h-4 w-4 text-muted-foreground" /> : <Eye className="h-4 w-4 text-muted-foreground" />}
                    </Button>
                  </div>
                  {/* Password strength indicators */}
                  <div className="flex flex-wrap gap-2 mt-2">
                    {passwordChecks.map((check) => (
                      <span
                        key={check.label}
                        className={`inline-flex items-center gap-1 text-xs px-2 py-1 rounded-full transition-colors ${
                          check.valid
                            ? 'bg-green-500/10 text-green-600 dark:text-green-400'
                            : 'bg-muted text-muted-foreground'
                        }`}
                      >
                        {check.valid && <CheckCircle className="h-3 w-3" />}
                        {check.label}
                      </span>
                    ))}
                  </div>
                </div>
                <div className="space-y-2">
                  <Label htmlFor="confirmPassword">Confirmar contraseña</Label>
                  <Input
                    id="confirmPassword"
                    type={showPassword ? 'text' : 'password'}
                    placeholder="••••••••"
                    className="h-11 bg-background/50 border-border/50"
                    data-testid="register-confirm-password-input"
                    {...form.register('confirmPassword')}
                  />
                  {form.formState.errors.confirmPassword && (
                    <p className="text-xs text-destructive">{form.formState.errors.confirmPassword.message}</p>
                  )}
                </div>
              </CardContent>
              <CardFooter className="flex flex-col gap-4 pb-6">
                <Button
                  type="submit"
                  className="w-full h-12 bg-gradient-to-r from-blue-500 to-purple-600 hover:from-blue-600 hover:to-purple-700 text-white font-medium shadow-lg shadow-purple-500/25"
                  disabled={isLoading}
                  data-testid="register-submit-button"
                >
                  {isLoading ? (
                    <Loader2 className="mr-2 h-5 w-5 animate-spin" />
                  ) : (
                    <Sparkles className="mr-2 h-5 w-5" />
                  )}
                  Crear mi cuenta
                </Button>
                <p className="text-sm text-muted-foreground text-center">
                  ¿Ya tienes cuenta?{' '}
                  <Link to="/login" className="text-accent hover:underline font-medium">
                    Inicia sesión
                  </Link>
                </p>
              </CardFooter>
            </form>
          </Card>
        </div>
      </div>
    </div>
  );
}
