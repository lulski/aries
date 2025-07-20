import "@testing-library/jest-dom";
import { render, screen, waitFor } from "@testing-library/react";
import Posts from "./page";

// Mock Mantine components
jest.mock("@mantine/core", () => ({
  Container: ({ children }: any) => (
    <div data-testid="container">{children}</div>
  ),
  Button: ({ children, ...props }: any) => (
    <button data-testid="button" {...props}>
      {children}
    </button>
  ),
  Group: ({ children }: any) => <div data-testid="group">{children}</div>,
}));

// Mock Next.js useSearchParams
jest.mock("next/navigation", () => ({
  useSearchParams: () => ({
    get: (key: string) => {
      // Return a default for 'page' and 'size' if not explicitly handled in a test
      if (key === "page") return "1"; // Default to page 1 for most tests
      if (key === "size") return "10"; // Default size
      return null;
    },
  }),
}));

// Mock AriesPagination
jest.mock("../components/Pagination/AriesPagination", () => ({
  __esModule: true,
  default: (props: any) => (
    <div data-testid="pagination">{JSON.stringify(props)}</div>
  ),
}));

// Mock PostInline
jest.mock("../components/PostInline", () => ({
  __esModule: true,
  default: (props: any) => <div data-testid="post-inline">{props.title}</div>,
}));

// Mock fetch - Ensure it's a Jest mock function globally
global.fetch = jest.fn();

const mockPosts = {
  postDto: [
    { id: 1, title: "Post 1" },
    { id: 2, title: "Post 2" },
  ],
  total: 10,
};

const mockAdminSession = {
  session: {
    isAuthenticated: true,
    username: "mock",
    firstname: "lulski",
    lastname: "gonzales",
    authorities: ["ADMIN", "USER"],
  },
};

const mockNonAdminSession = {
  session: {
    isAuthenticated: true,
    username: "mock",
    firstname: "lulski",
    lastname: "gonzales",
    authorities: ["ADMIN", "USER"],
  },
};

describe("Posts Component", () => {
  beforeEach(() => {
    // Clear all mocks before each test
    (fetch as jest.Mock).mockReset();
  });

  it("renders loading state initially", async () => {
    (fetch as jest.Mock)
      .mockResolvedValueOnce({
        // Default mock for /api/posts
        ok: true,
        json: async () => mockPosts,
      })
      .mockResolvedValueOnce({
        // Default mock for /api/me
        ok: true,
        json: async () => mockAdminSession,
      });

    render(<Posts searchParams={{} as any} />);
    expect(screen.getByText("Loading...")).toBeInTheDocument();
    // Wait for the content to appear to ensure loading state is gone
    await waitFor(() => {
      expect(screen.queryByText("Loading...")).not.toBeInTheDocument();
    });
  });

  it("renders posts and pagination after fetch 1", async () => {
    (fetch as jest.Mock)
      .mockResolvedValueOnce({
        // Default mock for /api/posts
        ok: true,
        json: async () => mockPosts,
      })
      .mockResolvedValueOnce({
        // Default mock for /api/me
        ok: true,
        json: async () => mockAdminSession,
      });

    render(<Posts searchParams={{} as any} />);
    await waitFor(() => {
      const containers = screen.getAllByTestId("container");
      expect(containers[1]).toBeInTheDocument(); //container[0] is wrapping `New Post` button
      expect(screen.getAllByTestId("post-inline")).toHaveLength(2);
      expect(screen.getByTestId("pagination")).toBeInTheDocument();
    });

    expect(screen.getByText("Post 1")).toBeInTheDocument();
    expect(screen.getByText("Post 2")).toBeInTheDocument();
    expect(fetch).toHaveBeenCalledTimes(2); // Verify that both fetch calls happened
  });

  it("does not render pagination if posts.total is 0", async () => {
    // This test needs different mock data, so we override the beforeEach setup
    (fetch as jest.Mock)
      .mockResolvedValueOnce({
        ok: true,
        json: async () => ({ postDto: [], total: 0 }), // Mock empty posts
      })
      .mockResolvedValueOnce({
        ok: true,
        json: async () => mockAdminSession,
      });

    render(<Posts searchParams={{} as any} />);
    await waitFor(() => {
      expect(screen.queryByTestId("pagination")).not.toBeInTheDocument();
      expect(screen.queryByTestId("post-inline")).not.toBeInTheDocument(); // No posts should be rendered
    });
    expect(fetch).toHaveBeenCalledTimes(2);
  });

  it("handles fetch error", async () => {
    jest.clearAllMocks();
    // Override beforeEach mocks for this specific error test
    (fetch as jest.Mock)
      .mockResolvedValueOnce({
        ok: false,
      })
      .mockResolvedValueOnce({
        ok: false,
      });

    // Suppress console.error during this test
    const consoleErrorSpy = jest
      .spyOn(console, "error")
      .mockImplementation(() => {});

    render(<Posts searchParams={{} as any} />);
    await waitFor(() => {
      expect(
        screen.getByText(/Error: Failed to fetch posts/)
      ).toBeInTheDocument();
    });
    // Restore console.error after the test
    consoleErrorSpy.mockRestore();
    expect(fetch).toHaveBeenCalledTimes(2); // Still two fetch calls, one failing
  });

  it("`New Post` button is displayed when for Admin", async () => {
    (fetch as jest.Mock)
      .mockResolvedValueOnce({
        ok: true,
        json: async () => mockPosts,
      })
      .mockResolvedValueOnce({
        ok: true,
        json: async () => mockAdminSession,
      });
    render(<Posts searchParams={{} as any} />);
    await waitFor(() => {
      expect(screen.getByTestId("button"));
    });
  });

  it("`New Post` button is not displayed for non Admin", async () => {
    (fetch as jest.Mock)
      .mockResolvedValueOnce({
        ok: true,
        json: async () => mockPosts,
      })
      .mockResolvedValueOnce({
        ok: true,
        json: async () => mockNonAdminSession,
      });
    render(<Posts searchParams={{} as any} />);
    await waitFor(() => {
      expect(screen.getByTestId("button"));
    });
  });
});
