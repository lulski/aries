import "@testing-library/jest-dom";
import { render, screen, waitFor } from "@testing-library/react";
import Posts from "./page";

// Mock Mantine components
jest.mock("@mantine/core", () => ({
  Container: ({ children }: any) => (
    <div data-testid="container">{children}</div>
  ),
}));

// Mock Next.js useSearchParams
jest.mock("next/navigation", () => ({
  useSearchParams: () => ({
    get: (key: string) => {
      if (key === "page") return "2";
      if (key === "size") return "5";
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

// Mock fetch
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

beforeEach(() => {
  (fetch as jest.Mock).mockClear();
});

test("renders loading state initially", async () => {
  (fetch as jest.Mock)
    .mockResolvedValueOnce({
      //mock /api/posts
      ok: true,
      json: async () => mockPosts,
    })
    .mockResolvedValueOnce({
      //mock /api/me
      ok: true,
      json: async () => mockAdminSession,
    });

  render(<Posts searchParams={{} as any} />);
  expect(screen.getByText("Loading...")).toBeInTheDocument();
});

test("renders posts and pagination after fetch", async () => {
  (fetch as jest.Mock)
    .mockResolvedValueOnce({
      ok: true,
      json: async () => mockPosts,
    })
    .mockResolvedValueOnce({
      //mock /api/me
      ok: true,
      json: async () => mockAdminSession,
    });

  render(<Posts searchParams={{} as any} />);
  await waitFor(() => {
    expect(screen.getAllByTestId("post-inline")).toHaveLength(2);
    expect(screen.getByTestId("pagination")).toBeInTheDocument();
    expect(screen.getByTestId("container")).toBeInTheDocument();
  });
  //console.log(screen.debug());

  expect(screen.getByText("Post 1")).toBeInTheDocument();
  expect(screen.getByText("Post 2")).toBeInTheDocument();
});

test("does not render pagination if posts.total is 0", async () => {
  (fetch as jest.Mock)
    .mockResolvedValueOnce({
      ok: true,
      json: async () => ({ ...mockPosts, total: 0 }),
    })
    .mockResolvedValueOnce({
      //mock /api/me
      ok: true,
      json: async () => mockAdminSession,
    });

  render(<Posts searchParams={{} as any} />);
  await waitFor(() => {
    expect(screen.queryByTestId("pagination")).not.toBeInTheDocument();
  });
});

test("handles fetch error", async () => {
  (fetch as jest.Mock)
    .mockResolvedValueOnce({
      ok: false,
    })
    .mockResolvedValueOnce({
      //mock /api/me
      ok: true,
      json: async () => mockNonAdminSession,
    });

  // Suppress error output
  jest.spyOn(console, "error").mockImplementation(() => {});

  render(<Posts searchParams={{} as any} />);
  await waitFor(() => {
    console.log(screen.debug());
    expect(
      screen.getByText(/Error: Failed to fetch posts/)
    ).toBeInTheDocument();
  });
  (console.error as jest.Mock).mockRestore();
});
