"use client";

import { useEffect, useState, createContext, useContext } from "react";
import { useRouter } from "next/navigation";
import { Loader, Center } from "@mantine/core";
import { SessionData } from "@/app/lib/definitions";
import { SessionProvider } from "../context/SessionContext";

type Props = {
  children: React.ReactNode;
};


export default function AuthGuard({ children }: Props ) {
  const [session, setSession] = useState<SessionData | null>(null);
  const [loading, setLoading] = useState(true);
  const router = useRouter();

  useEffect(() => {
    
    const checkAuth = async () => {
      try {
        const res = await fetch("/api/me");

        //fail api call should trigger an error 
        if (!res.ok) throw new Error("Not authenticated");

        const data:SessionData = await res.json();

        console.log("Session data:", data);
              
        setSession(data);
        setLoading(false);
                
      } catch (err) {
        router.replace("/login");
      }
    };
    
    checkAuth();
  }, [router]);

  if (loading || !session) {
    return (
      <Center h="100vh">
        <Loader />
      </Center>
    );
  }

  return (<SessionProvider value={session}>
    {children}
  </SessionProvider>);
}
