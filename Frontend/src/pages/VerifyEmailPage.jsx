import { useState, useEffect } from 'react';
import { Link, useSearchParams, useNavigate } from 'react-router-dom';
import { authApi } from '@/api/auth';
import { Button } from '@/components/ui/button';
import { Card, CardDescription, CardFooter, CardHeader, CardTitle } from '@/components/ui/card';
import { Loader2, CheckCircle, XCircle, Film } from 'lucide-react';

export function VerifyEmailPage() {
  const [status, setStatus] = useState('loading'); // loading, success, error
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const token = searchParams.get('token');

  useEffect(() => {
    const verifyEmail = async () => {
      if (!token) {
        setStatus('error');
        return;
      }

      try {
        await authApi.verifyEmail(token);
        setStatus('success');
      } catch (error) {
        setStatus('error');
      }
    };

    verifyEmail();
  }, [token]);

  return (
    <div className="min-h-screen flex items-center justify-center bg-background p-4">
      <Card className="w-full max-w-md">
        <CardHeader className="text-center space-y-4">
          <div className="flex justify-center">
            <div className={`p-3 rounded-xl ${
              status === 'loading' ? 'bg-accent/10' :
              status === 'success' ? 'bg-green-100 dark:bg-green-900/20' :
              'bg-red-100 dark:bg-red-900/20'
            }`}>
              {status === 'loading' && <Loader2 className="h-8 w-8 text-accent animate-spin" />}
              {status === 'success' && <CheckCircle className="h-8 w-8 text-green-500" />}
              {status === 'error' && <XCircle className="h-8 w-8 text-red-500" />}
            </div>
          </div>
          <CardTitle className="font-outfit text-2xl">
            {status === 'loading' && 'Verificando email...'}
            {status === 'success' && 'Email verificado'}
            {status === 'error' && 'Error de verificación'}
          </CardTitle>
          <CardDescription>
            {status === 'loading' && 'Por favor espera mientras verificamos tu email'}
            {status === 'success' && 'Tu email ha sido verificado exitosamente. Ya puedes iniciar sesión.'}
            {status === 'error' && 'El enlace de verificación es inválido o ha expirado.'}
          </CardDescription>
        </CardHeader>
        <CardFooter>
          {status !== 'loading' && (
            <Button
              className="w-full bg-accent hover:bg-accent/90"
              onClick={() => navigate('/login')}
              data-testid="verify-email-login-button"
            >
              Ir a iniciar sesión
            </Button>
          )}
        </CardFooter>
      </Card>
    </div>
  );
}
