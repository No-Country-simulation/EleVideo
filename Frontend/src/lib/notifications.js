// Request notification permission and show notifications
export const requestNotificationPermission = async () => {
  if (!('Notification' in window)) {
    console.log('This browser does not support notifications');
    return false;
  }

  if (Notification.permission === 'granted') {
    return true;
  }

  if (Notification.permission !== 'denied') {
    const permission = await Notification.requestPermission();
    return permission === 'granted';
  }

  return false;
};

export const showNotification = (title, options = {}) => {
  if (Notification.permission === 'granted') {
    const notification = new Notification(title, {
      icon: '/favicon.ico',
      badge: '/favicon.ico',
      ...options,
    });

    notification.onclick = () => {
      window.focus();
      notification.close();
    };

    // Auto close after 5 seconds
    setTimeout(() => notification.close(), 5000);

    return notification;
  }
};

export const notifyProcessingComplete = (videoTitle, status) => {
  const isSuccess = status?.toLowerCase() === 'completed';
  
  showNotification(
    isSuccess ? 'Video procesado' : 'Error de procesamiento',
    {
      body: isSuccess 
        ? `"${videoTitle}" está listo para descargar`
        : `Hubo un error procesando "${videoTitle}"`,
      tag: 'processing-complete',
      requireInteraction: true,
    }
  );
};
