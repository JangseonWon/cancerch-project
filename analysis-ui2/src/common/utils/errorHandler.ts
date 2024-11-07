type FunctionType = (...args: any[]) => any;

export const errorHandler = <T extends FunctionType>(someFunction: T): T => {
    const wrappedFunction: FunctionType = async (...args: any[]) => {
        try {
            return await (someFunction(...args) as Promise<any>);
        } catch (e) {
            const error = e as Error;
            console.error(`An error occurred: ${error.message}`);
        }
    };

    return wrappedFunction as T;
};