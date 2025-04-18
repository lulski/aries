'use client';

import { createContext, useContext } from 'react';
import type { SessionData } from '@/app/lib/definitions';

// Create the context
const SessionContext = createContext<SessionData | undefined>(undefined);

// Export a hook to use it
export const useSession = () => {
  const session = useContext(SessionContext);
  if (!session) {
    throw new Error('useSession must be used within a SessionProvider');
  }
  return session;
};

// Export the provider
export const SessionProvider = SessionContext.Provider;
