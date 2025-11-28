# 🎨 Frontend Tech Stack - React E-Commerce Platform

## 📋 Backend Analysis
Backend của bạn bao gồm:
- **Authentication**: JWT, OAuth2 (Google, GitHub, Facebook), OTP verification
- **E-Commerce Features**: Products, Categories, Product Images (Cloudinary)
- **Payment**: VNPay integration
- **Real-time**: WebSocket support
- **Security**: Spring Security with role-based access control
- **API**: RESTful API với Swagger documentation

---

## 🚀 Recommended Frontend Tech Stack

### **Core Framework & Language**
```json
{
  "react": "^18.3.1",
  "typescript": "^5.5.4",
  "vite": "^5.4.0"
}
```
**Lý do**: 
- React 18 với Concurrent Features
- TypeScript cho type safety
- Vite cho build siêu nhanh (thay vì CRA)

---

### **🎨 UI Library - SHADCN/UI + TAILWIND CSS** ⭐ (HIGHLY RECOMMENDED)
```json
{
  "tailwindcss": "^3.4.1",
  "@radix-ui/react-*": "latest",
  "class-variance-authority": "^0.7.0",
  "clsx": "^2.1.0",
  "tailwind-merge": "^2.2.0",
  "lucide-react": "^0.344.0"
}
```

**Cài đặt Shadcn/ui**:
```bash
npx shadcn-ui@latest init
npx shadcn-ui@latest add button
npx shadcn-ui@latest add card
npx shadcn-ui@latest add dialog
npx shadcn-ui@latest add dropdown-menu
npx shadcn-ui@latest add form
npx shadcn-ui@latest add input
npx shadcn-ui@latest add select
npx shadcn-ui@latest add table
npx shadcn-ui@latest add toast
npx shadcn-ui@latest add avatar
npx shadcn-ui@latest add badge
npx shadcn-ui@latest add skeleton
```

**Ưu điểm**:
- ✅ UI CỰC ĐẸP, hiện đại nhất 2024-2025
- ✅ Copy component vào project, customize 100%
- ✅ Built on Radix UI (accessible, keyboard navigation)
- ✅ Dark mode support sẵn
- ✅ Animation mượt mà với Tailwind
- ✅ Không phải import cả library như MUI/AntD

**Alternative Options**:
- **Aceternity UI**: Components với animation 3D cực đẹp
- **Magic UI**: Pre-built landing page components
- **NextUI**: Modern, fast, beautiful (nếu muốn ready-to-use)

---

### **🔐 Authentication & State Management**

#### **Zustand** (State Management) - RECOMMENDED
```json
{
  "zustand": "^4.5.0"
}
```
**Lý do**: Simple, lightweight, TypeScript-friendly

#### **TanStack Query (React Query)** - DATA FETCHING ⭐
```json
{
  "@tanstack/react-query": "^5.28.0",
  "@tanstack/react-query-devtools": "^5.28.0"
}
```
**Lý do**: 
- Server state management
- Auto caching, refetching
- Optimistic updates
- Perfect cho REST API

#### **Axios** + **Axios Interceptors**
```json
{
  "axios": "^1.6.7"
}
```

**Setup Authentication Store với Zustand**:
```typescript
// stores/authStore.ts
import { create } from 'zustand';
import { persist } from 'zustand/middleware';

interface AuthState {
  accessToken: string | null;
  refreshToken: string | null;
  user: User | null;
  setTokens: (access: string, refresh: string) => void;
  setUser: (user: User) => void;
  logout: () => void;
}

export const useAuthStore = create<AuthState>()(
  persist(
    (set) => ({
      accessToken: null,
      refreshToken: null,
      user: null,
      setTokens: (access, refresh) => 
        set({ accessToken: access, refreshToken: refresh }),
      setUser: (user) => set({ user }),
      logout: () => set({ accessToken: null, refreshToken: null, user: null })
    }),
    { name: 'auth-storage' }
  )
);
```

---

### **🛣️ Routing**
```json
{
  "@tanstack/react-router": "^1.19.0"
}
```
**Or** traditional:
```json
{
  "react-router-dom": "^6.22.0"
}
```

---

### **📝 Form Handling**
```json
{
  "react-hook-form": "^7.50.0",
  "zod": "^3.22.4",
  "@hookform/resolvers": "^3.3.4"
}
```

**Example Form với Shadcn/ui**:
```typescript
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Form, FormControl, FormField, FormItem, FormLabel } from "@/components/ui/form";

const loginSchema = z.object({
  usernameOrEmail: z.string().min(1, "Required"),
  password: z.string().min(6, "Min 6 characters")
});

function LoginForm() {
  const form = useForm({
    resolver: zodResolver(loginSchema),
    defaultValues: { usernameOrEmail: "", password: "" }
  });

  const onSubmit = (data: z.infer<typeof loginSchema>) => {
    // API call
  };

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
        <FormField
          control={form.control}
          name="usernameOrEmail"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Username or Email</FormLabel>
              <FormControl>
                <Input placeholder="Enter username..." {...field} />
              </FormControl>
            </FormItem>
          )}
        />
        <Button type="submit">Login</Button>
      </form>
    </Form>
  );
}
```

---

### **📸 Image Upload & Management**
```json
{
  "react-dropzone": "^14.2.3",
  "react-image-crop": "^11.0.5"
}
```

**For Cloudinary**:
```typescript
// utils/cloudinary.ts
export const uploadToCloudinary = async (file: File, productId: string) => {
  const formData = new FormData();
  formData.append('file', file);
  formData.append('isPrimary', 'false');

  const response = await axios.post(
    `/api/v1/products/${productId}/images`,
    formData,
    {
      headers: { 'Content-Type': 'multipart/form-data' }
    }
  );
  return response.data;
};
```

---

### **💳 Payment Integration (VNPay)**
```typescript
// services/payment.ts
export const createVNPayPayment = async (amount: number, bankCode: string) => {
  const response = await axios.post('/vnpay/create-payment', {
    amount,
    bankCode,
    language: 'vn'
  });
  // Redirect to payment URL
  window.location.href = response.data.paymentUrl;
};
```

---

### **🔔 Real-time (WebSocket)**
```json
{
  "@stomp/stompjs": "^7.0.0",
  "sockjs-client": "^1.6.1"
}
```

```typescript
// hooks/useWebSocket.ts
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

export const useWebSocket = () => {
  const client = new Client({
    webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
    onConnect: () => {
      client.subscribe('/topic/notifications', (message) => {
        console.log('Received:', message.body);
      });
    }
  });

  useEffect(() => {
    client.activate();
    return () => client.deactivate();
  }, []);
};
```

---

### **🎭 Animation Libraries**
```json
{
  "framer-motion": "^11.0.5",
  "react-spring": "^9.7.3"
}
```

**Example với Framer Motion**:
```typescript
import { motion } from "framer-motion";

function ProductCard({ product }) {
  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      whileHover={{ scale: 1.05 }}
      className="rounded-lg border p-4"
    >
      <img src={product.imageUrl} alt={product.name} />
      <h3>{product.name}</h3>
      <p>${product.price}</p>
    </motion.div>
  );
}
```

---

### **📊 Data Visualization (for Admin Dashboard)**
```json
{
  "recharts": "^2.12.0",
  "@tremor/react": "^3.14.1"
}
```

---

### **🌐 Internationalization (i18n)**
```json
{
  "react-i18next": "^14.0.5",
  "i18next": "^23.10.0"
}
```

**Setup i18n**:
```typescript
// i18n.ts
import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';

i18n
  .use(initReactI18next)
  .init({
    resources: {
      en: { translation: { welcome: "Welcome" } },
      vi: { translation: { welcome: "Chào mừng" } }
    },
    lng: 'vi',
    fallbackLng: 'en'
  });
```

---

### **🔍 Search & Filtering**
```json
{
  "fuse.js": "^7.0.0"
}
```

---

### **📱 Mobile-First & Responsive**
```json
{
  "react-responsive": "^10.0.0"
}
```

---

### **🧪 Testing**
```json
{
  "vitest": "^1.3.1",
  "@testing-library/react": "^14.2.1",
  "@testing-library/jest-dom": "^6.4.2",
  "msw": "^2.1.5"
}
```

---

### **🔧 Developer Tools**
```json
{
  "eslint": "^8.57.0",
  "prettier": "^3.2.5",
  "husky": "^9.0.11",
  "lint-staged": "^15.2.2"
}
```

---

## 📦 Complete package.json

```json
{
  "name": "ecommerce-frontend",
  "version": "1.0.0",
  "type": "module",
  "scripts": {
    "dev": "vite",
    "build": "tsc && vite build",
    "preview": "vite preview",
    "lint": "eslint . --ext ts,tsx --report-unused-disable-directives --max-warnings 0"
  },
  "dependencies": {
    "react": "^18.3.1",
    "react-dom": "^18.3.1",
    "react-router-dom": "^6.22.0",
    "@tanstack/react-query": "^5.28.0",
    "axios": "^1.6.7",
    "zustand": "^4.5.0",
    "react-hook-form": "^7.50.0",
    "zod": "^3.22.4",
    "@hookform/resolvers": "^3.3.4",
    "tailwindcss": "^3.4.1",
    "class-variance-authority": "^0.7.0",
    "clsx": "^2.1.0",
    "tailwind-merge": "^2.2.0",
    "lucide-react": "^0.344.0",
    "framer-motion": "^11.0.5",
    "react-dropzone": "^14.2.3",
    "@stomp/stompjs": "^7.0.0",
    "sockjs-client": "^1.6.1",
    "react-i18next": "^14.0.5",
    "i18next": "^23.10.0",
    "recharts": "^2.12.0",
    "sonner": "^1.4.3"
  },
  "devDependencies": {
    "@types/react": "^18.2.64",
    "@types/react-dom": "^18.2.21",
    "@typescript-eslint/eslint-plugin": "^7.1.1",
    "@typescript-eslint/parser": "^7.1.1",
    "@vitejs/plugin-react-swc": "^3.6.0",
    "typescript": "^5.5.4",
    "vite": "^5.4.0",
    "autoprefixer": "^10.4.18",
    "postcss": "^8.4.35",
    "eslint": "^8.57.0",
    "prettier": "^3.2.5",
    "vitest": "^1.3.1"
  }
}
```

---

## 🏗️ Recommended Project Structure

```
src/
├── api/
│   ├── axios.ts              # Axios config + interceptors
│   ├── auth.api.ts
│   ├── product.api.ts
│   └── payment.api.ts
├── components/
│   ├── ui/                   # Shadcn components
│   ├── layout/
│   │   ├── Header.tsx
│   │   ├── Footer.tsx
│   │   └── Sidebar.tsx
│   ├── auth/
│   │   ├── LoginForm.tsx
│   │   └── RegisterForm.tsx
│   ├── product/
│   │   ├── ProductCard.tsx
│   │   ├── ProductList.tsx
│   │   └── ProductDetail.tsx
│   └── common/
│       ├── Loading.tsx
│       └── ErrorBoundary.tsx
├── pages/
│   ├── Home.tsx
│   ├── Products.tsx
│   ├── ProductDetail.tsx
│   ├── Cart.tsx
│   ├── Checkout.tsx
│   ├── Login.tsx
│   ├── Register.tsx
│   └── admin/
│       ├── Dashboard.tsx
│       ├── ProductManagement.tsx
│       └── OrderManagement.tsx
├── hooks/
│   ├── useAuth.ts
│   ├── useProducts.ts
│   ├── useCart.ts
│   └── useWebSocket.ts
├── stores/
│   ├── authStore.ts
│   ├── cartStore.ts
│   └── uiStore.ts
├── types/
│   ├── auth.types.ts
│   ├── product.types.ts
│   └── common.types.ts
├── utils/
│   ├── format.ts
│   ├── validation.ts
│   └── constants.ts
├── lib/
│   └── utils.ts              # Shadcn utils
├── App.tsx
└── main.tsx
```

---

## 🎯 API Integration Example

### Setup Axios with Interceptors
```typescript
// api/axios.ts
import axios from 'axios';
import { useAuthStore } from '@/stores/authStore';

const api = axios.create({
  baseURL: 'http://localhost:8080/api/v1',
  headers: {
    'Content-Type': 'application/json'
  }
});

// Request interceptor
api.interceptors.request.use(
  (config) => {
    const { accessToken } = useAuthStore.getState();
    if (accessToken) {
      config.headers.Authorization = `Bearer ${accessToken}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Response interceptor for refresh token
api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;
    
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;
      
      try {
        const { refreshToken } = useAuthStore.getState();
        const response = await axios.post('/api/v1/auth/refresh-token', {
          refreshToken
        });
        
        const { accessToken } = response.data;
        useAuthStore.getState().setTokens(accessToken, refreshToken);
        
        originalRequest.headers.Authorization = `Bearer ${accessToken}`;
        return api(originalRequest);
      } catch (refreshError) {
        useAuthStore.getState().logout();
        window.location.href = '/login';
        return Promise.reject(refreshError);
      }
    }
    
    return Promise.reject(error);
  }
);

export default api;
```

### Product API with React Query
```typescript
// api/product.api.ts
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import api from './axios';
import type { Product, ProductRequest } from '@/types/product.types';

export const useProducts = (filters?: {
  name?: string;
  priceFrom?: number;
  priceTo?: number;
  page?: number;
  size?: number;
}) => {
  return useQuery({
    queryKey: ['products', filters],
    queryFn: async () => {
      const { data } = await api.post('/products/search', null, {
        params: filters
      });
      return data;
    }
  });
};

export const useCreateProduct = () => {
  const queryClient = useQueryClient();
  
  return useMutation({
    mutationFn: async (product: ProductRequest) => {
      const { data } = await api.post('/products', product);
      return data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['products'] });
    }
  });
};

export const useUploadProductImage = () => {
  return useMutation({
    mutationFn: async ({ 
      productId, 
      file, 
      isPrimary 
    }: { 
      productId: string; 
      file: File; 
      isPrimary: boolean 
    }) => {
      const formData = new FormData();
      formData.append('file', file);
      formData.append('isPrimary', String(isPrimary));
      
      const { data } = await api.post(
        `/products/${productId}/images`,
        formData,
        {
          headers: { 'Content-Type': 'multipart/form-data' }
        }
      );
      return data;
    }
  });
};
```

---

## 🎨 UI Component Examples

### Modern Product Card với Shadcn/ui
```typescript
import { Card, CardContent, CardFooter } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { ShoppingCart, Heart } from "lucide-react";
import { motion } from "framer-motion";

interface ProductCardProps {
  product: Product;
}

export function ProductCard({ product }: ProductCardProps) {
  return (
    <motion.div
      whileHover={{ y: -5 }}
      transition={{ duration: 0.2 }}
    >
      <Card className="overflow-hidden group">
        <div className="relative aspect-square overflow-hidden">
          <img
            src={product.imageUrl}
            alt={product.name}
            className="object-cover w-full h-full group-hover:scale-110 transition-transform duration-300"
          />
          <Button
            size="icon"
            variant="ghost"
            className="absolute top-2 right-2 bg-white/80 backdrop-blur-sm"
          >
            <Heart className="h-4 w-4" />
          </Button>
          {!product.isActive && (
            <Badge className="absolute top-2 left-2" variant="destructive">
              Out of Stock
            </Badge>
          )}
        </div>
        
        <CardContent className="p-4">
          <h3 className="font-semibold text-lg line-clamp-2">
            {product.name}
          </h3>
          <p className="text-sm text-muted-foreground mt-1 line-clamp-2">
            {product.description}
          </p>
          <div className="flex items-center justify-between mt-3">
            <span className="text-2xl font-bold text-primary">
              ${product.price}
            </span>
            <Badge variant="secondary">
              {product.stockQuantity} left
            </Badge>
          </div>
        </CardContent>
        
        <CardFooter className="p-4 pt-0">
          <Button className="w-full" disabled={!product.isActive}>
            <ShoppingCart className="mr-2 h-4 w-4" />
            Add to Cart
          </Button>
        </CardFooter>
      </Card>
    </motion.div>
  );
}
```

---

## 🚀 Getting Started Commands

```bash
# 1. Create Vite + React + TypeScript project
npm create vite@latest ecommerce-frontend -- --template react-ts

cd ecommerce-frontend

# 2. Install dependencies
npm install

# 3. Install Shadcn/ui
npx shadcn-ui@latest init

# 4. Install other dependencies
npm install @tanstack/react-query axios zustand react-hook-form zod @hookform/resolvers framer-motion react-router-dom react-dropzone @stomp/stompjs sockjs-client react-i18next

# 5. Install dev dependencies
npm install -D @types/sockjs-client

# 6. Start development server
npm run dev
```

---

## 🎯 Key Features to Implement

### 1. **Authentication Flow**
- ✅ Login with JWT
- ✅ OAuth2 (Google, GitHub, Facebook)
- ✅ OTP Verification
- ✅ Password Reset
- ✅ Auto refresh token
- ✅ Protected routes

### 2. **Product Management**
- ✅ Product listing with filters
- ✅ Search & sorting
- ✅ Product detail page
- ✅ Image upload with Cloudinary
- ✅ Multi-image support
- ✅ Stock management

### 3. **Shopping Cart**
- ✅ Add/remove items
- ✅ Update quantities
- ✅ Persistent cart (localStorage/Zustand)
- ✅ Cart summary

### 4. **Checkout & Payment**
- ✅ VNPay integration
- ✅ Order summary
- ✅ Payment confirmation
- ✅ Order history

### 5. **Admin Dashboard**
- ✅ Product management (CRUD)
- ✅ Order management
- ✅ User management
- ✅ Analytics dashboard
- ✅ Role-based access

### 6. **Real-time Features**
- ✅ Notifications via WebSocket
- ✅ Live order updates
- ✅ Stock updates

---

## 🎨 Design System & Themes

### Color Palette (Tailwind Config)
```javascript
// tailwind.config.js
module.exports = {
  theme: {
    extend: {
      colors: {
        primary: {
          50: '#f0f9ff',
          500: '#0ea5e9',
          600: '#0284c7',
          700: '#0369a1',
        },
        // Add more custom colors
      }
    }
  }
}
```

### Dark Mode Support
Shadcn/ui đã support dark mode sẵn, chỉ cần toggle:
```typescript
import { Moon, Sun } from "lucide-react";
import { Button } from "@/components/ui/button";

function ThemeToggle() {
  const [theme, setTheme] = useState<'light' | 'dark'>('light');

  useEffect(() => {
    document.documentElement.classList.toggle('dark', theme === 'dark');
  }, [theme]);

  return (
    <Button
      variant="ghost"
      size="icon"
      onClick={() => setTheme(theme === 'light' ? 'dark' : 'light')}
    >
      {theme === 'light' ? <Moon /> : <Sun />}
    </Button>
  );
}
```

---

## 📚 Additional Resources

- **Shadcn/ui**: https://ui.shadcn.com/
- **Aceternity UI**: https://ui.aceternity.com/
- **TanStack Query**: https://tanstack.com/query
- **Framer Motion**: https://www.framer.com/motion/
- **Zustand**: https://github.com/pmndrs/zustand
- **React Hook Form**: https://react-hook-form.com/

---

## 💡 Pro Tips

1. **Use Shadcn/ui CLI** để add components nhanh chóng
2. **Implement Optimistic Updates** với React Query cho UX tốt hơn
3. **Use Framer Motion** cho page transitions mượt mà
4. **Implement Skeleton Loading** thay vì spinner
5. **Use Sonner** cho toast notifications (đẹp hơn react-toastify)
6. **Implement Error Boundaries** để handle errors gracefully
7. **Use React.lazy() + Suspense** cho code splitting
8. **Implement PWA** nếu muốn mobile app experience

---

## 🎯 Summary

**Best Stack cho 2025:**
- ⚛️ **React 18** + **TypeScript** + **Vite**
- 🎨 **Shadcn/ui** + **Tailwind CSS** (UI đẹp nhất hiện nay)
- 🔄 **TanStack Query** (Data fetching)
- 🐻 **Zustand** (State management)
- 📝 **React Hook Form** + **Zod** (Form validation)
- ✨ **Framer Motion** (Animations)
- 🔐 **Axios Interceptors** (Auto refresh token)

**This stack will give you:**
- 🚀 Blazing fast performance
- 🎨 Beautiful, modern UI
- 📱 Mobile-first responsive
- ♿ Accessible (WCAG compliant)
- 🌙 Dark mode support
- 🔒 Secure authentication
- 💪 Type-safe with TypeScript
- 🎯 Easy to maintain and scale


