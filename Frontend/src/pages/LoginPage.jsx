import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { useAuth } from '@/context/AuthContext';
import { useTheme } from '@/context/ThemeContext';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from '@/components/ui/card';
import { toast } from 'sonner';
import { Film, Loader2, Eye, EyeOff, Moon, Sun, Sparkles, ArrowRight } from 'lucide-react';

const loginSchema = z.object({
  email: z.string().email('Email inválido'),
  password: z.string().min(8, 'La contraseña debe tener al menos 8 caracteres'),
});

export function LoginPage() {
  const [isLoading, setIsLoading] = useState(false);
  const [showPassword, setShowPassword] = useState(false);
  const { login } = useAuth();
  const { theme, toggleTheme } = useTheme();
  const navigate = useNavigate();

  const form = useForm({
    resolver: zodResolver(loginSchema),
    defaultValues: {
      email: '',
      password: '',
    },
  });

  const onSubmit = async (data) => {
    setIsLoading(true);
    try {
      await login(data);
      toast.success('¡Bienvenido de vuelta!');
      navigate('/dashboard');
    } catch (error) {
      const message = error.response?.data?.message || 'Error al iniciar sesión';
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
      <div className="relative z-10 min-h-screen flex items-center justify-center p-4">
        <div className="w-full max-w-md space-y-8">
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
                Bienvenido a <span className="gradient-text">Elevideo</span>
              </h1>
              <p className="text-muted-foreground mt-2">
                Convierte tus videos horizontales a verticales con IA
              </p>
            </div>
          </div>

          {/* Card */}
          <Card className="border-border/50 bg-card/80 backdrop-blur-xl shadow-2xl">
            <form onSubmit={form.handleSubmit(onSubmit)}>
              <CardContent className="space-y-5 pt-6">
                <div className="space-y-2">
                  <Label htmlFor="email" className="text-sm font-medium">
                    Email
                  </Label>
                  <Input
                    id="email"
                    type="email"
                    placeholder="tu@email.com"
                    className="h-12 bg-background/50 border-border/50 focus:border-accent"
                    data-testid="login-email-input"
                    tabIndex={1}
                    autoFocus
                    {...form.register('email')}
                  />
                  {form.formState.errors.email && (
                    <p className="text-sm text-destructive">{form.formState.errors.email.message}</p>
                  )}
                </div>
                <div className="space-y-2">
                  <Label htmlFor="password" className="text-sm font-medium">
                    Contraseña
                  </Label>
                  <div className="relative">
                    <Input
                      id="password"
                      type={showPassword ? 'text' : 'password'}
                      placeholder="••••••••"
                      className="h-12 bg-background/50 border-border/50 focus:border-accent pr-12"
                      data-testid="login-password-input"
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
                  {form.formState.errors.password && (
                    <p className="text-sm text-destructive">{form.formState.errors.password.message}</p>
                  )}
                </div>
                <div className="flex justify-end">
                  <Link 
                    to="/forgot-password" 
                    className="text-sm text-muted-foreground hover:text-accent transition-colors"
                  >
                    ¿Olvidaste tu contraseña?
                  </Link>
                </div>
              </CardContent>
              <CardFooter className="flex flex-col gap-4 pb-6">
                <Button
                  type="submit"
                  className="w-full h-12 bg-gradient-to-r from-blue-500 to-purple-600 hover:from-blue-600 hover:to-purple-700 text-white font-medium shadow-lg shadow-purple-500/25 transition-all hover:shadow-purple-500/40"
                  disabled={isLoading}
                  data-testid="login-submit-button"
                >
                  {isLoading ? (
                    <Loader2 className="mr-2 h-5 w-5 animate-spin" />
                  ) : (
                    <Sparkles className="mr-2 h-5 w-5" />
                  )}
                  Iniciar sesión
                </Button>
                <p className="text-sm text-muted-foreground text-center">
                  ¿No tienes cuenta?{' '}
                  <Link to="/register" className="text-accent hover:underline font-medium">
                    Regístrate gratis
                  </Link>
                </p>
              </CardFooter>
            </form>
          </Card>

          {/* Features */}
          <div className="grid grid-cols-3 gap-4 text-center text-xs text-muted-foreground">
            <div className="space-y-1">
              <div className="w-8 h-8 mx-auto rounded-lg bg-blue-500/10 flex items-center justify-center">
                <Film className="h-4 w-4 text-blue-500" />
              </div>
              <p>Smart Crop</p>
            </div>
            <div className="space-y-1">
              <div className="w-8 h-8 mx-auto rounded-lg bg-purple-500/10 flex items-center justify-center">
                <Sparkles className="h-4 w-4 text-purple-500" />
              </div>
              <p>IA Automática</p>
            </div>
            <div className="space-y-1">
              <div className="w-8 h-8 mx-auto rounded-lg bg-pink-500/10 flex items-center justify-center">
                <ArrowRight className="h-4 w-4 text-pink-500" />
              </div>
              <p>Shorts Fácil</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
