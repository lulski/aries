// components/AuthGuard.tsx
"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { Loader, Center } from "@mantine/core";
import { SessionData } from "../lib/definitions";

type Props = {
  children: (session: SessionData) => React.ReactNode;
};

export default function AuthGuard({ children }: Props) {
  const [loading, setLoading] = useState(true);
  const router = useRouter();
  const [session, setSession] = useState<SessionData | null>(null);

  useEffect(() => {
    const checkAuth = async () => {
      try {
        const res = await fetch("/api/me");

        if (!res.ok) throw new Error("Not authenticated");

        const data = await res.json();
        console.log("Session data:", data.session);
        setSession(data.session);

        setLoading(false);
      } catch (err) {
        router.replace("/login");
      }
    };

    checkAuth();
  }, [router]);

  if (loading) {
    return (
      <Center h="100vh">
        <Loader />
      </Center>
    );
  }

  if (!session?.isAuthenticated) return null;

  return <>{children}</>;
}
