import { useState } from 'react';
import { Link } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { authApi } from '@/api/auth';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from '@/components/ui/card';
import { toast } from 'sonner';
import { Film, Loader2, ArrowLeft, Mail } from 'lucide-react';

const forgotPasswordSchema = z.object({
  email: z.string().email('Email inválido'),
});

export function ForgotPasswordPage() {
  const [isLoading, setIsLoading] = useState(false);
  const [isSubmitted, setIsSubmitted] = useState(false);

  const form = useForm({
    resolver: zodResolver(forgotPasswordSchema),
    defaultValues: {
      email: '',
    },
  });

  const onSubmit = async (data) => {
    setIsLoading(true);
    try {
      await authApi.forgotPassword(data.email);
      setIsSubmitted(true);
      toast.success('Email enviado');
    } catch (error) {
      // La API siempre devuelve 200 por seguridad
      setIsSubmitted(true);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-background p-4">
      <Card className="w-full max-w-md">
        <CardHeader className="text-center space-y-4">
          <div className="flex justify-center">
            <div className="p-3 rounded-xl bg-accent/10">
              {isSubmitted ? <Mail className="h-8 w-8 text-accent" /> : <Film className="h-8 w-8 text-accent" />}
            </div>
          </div>
          <CardTitle className="font-outfit text-2xl">
            {isSubmitted ? 'Revisa tu email' : 'Recuperar contraseña'}
          </CardTitle>
          <CardDescription>
            {isSubmitted
              ? 'Te hemos enviado un enlace para restablecer tu contraseña'
              : 'Ingresa tu email y te enviaremos un enlace para restablecer tu contraseña'}
          </CardDescription>
        </CardHeader>
        {!isSubmitted ? (
          <form onSubmit={form.handleSubmit(onSubmit)}>
            <CardContent className="space-y-4">
              <div className="space-y-2">
                <Label htmlFor="email">Email</Label>
                <Input
                  id="email"
                  type="email"
                  placeholder="tu@email.com"
                  data-testid="forgot-password-email-input"
                  {...form.register('email')}
                />
                {form.formState.errors.email && (
                  <p className="text-sm text-destructive">{form.formState.errors.email.message}</p>
                )}
              </div>
            </CardContent>
            <CardFooter className="flex flex-col gap-4">
              <Button
                type="submit"
                className="w-full bg-accent hover:bg-accent/90"
                disabled={isLoading}
                data-testid="forgot-password-submit-button"
              >
                {isLoading && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
                Enviar enlace
              </Button>
              <Link
                to="/login"
                className="flex items-center justify-center gap-2 text-sm text-muted-foreground hover:text-accent"
              >
                <ArrowLeft className="h-4 w-4" />
                Volver al inicio de sesión
              </Link>
            </CardFooter>
          </form>
        ) : (
          <CardFooter className="flex flex-col gap-4">
            <Button
              className="w-full bg-accent hover:bg-accent/90"
              onClick={() => setIsSubmitted(false)}
              data-testid="forgot-password-resend-button"
            >
              Enviar de nuevo
            </Button>
            <Link
              to="/login"
              className="flex items-center justify-center gap-2 text-sm text-muted-foreground hover:text-accent"
            >
              <ArrowLeft className="h-4 w-4" />
              Volver al inicio de sesión
            </Link>
          </CardFooter>
        )}
      </Card>
    </div>
  );
}
