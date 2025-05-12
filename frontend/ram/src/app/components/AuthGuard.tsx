"use client";

import { useEffect, useState } from "react";
import { useRouter, usePathname } from "next/navigation";
import { Loader, Center, LoadingOverlay } from "@mantine/core";
import { SessionData } from "@/app/lib/definitions";
import { SessionProvider } from "../context/SessionContext";

type Props = {
  children: React.ReactNode;
};

export default function AuthGuard({ children }: Props) {
  const [session, setSession] = useState<SessionData | null>(null);
  const [loading, setLoading] = useState(true);
  const router = useRouter();
  const pathname = usePathname();

  useEffect(() => {
    const checkAuth = async () => {
      try {
        const res = await fetch("/api/me");

        if (!res.ok) throw new Error("Not authenticated");

        const data: SessionData = await res.json();

        console.log("Session data:", data);

        setSession(data);
        setLoading(false);
      } catch (err) {
        // Encode the current path to handle special characters
        const returnUrl = encodeURIComponent(pathname);
        router.replace(`/login?returnUrl=${returnUrl}`);
      }
    };

    checkAuth();
  }, [router, pathname]);

  if (loading || !session) {
    return (
      <LoadingOverlay
        zIndex={1000}
        overlayProps={{ radius: "sm", blur: 2 }}
        visible={true}
      />
    );
  }

  return <SessionProvider value={session}>{children}</SessionProvider>;
}
